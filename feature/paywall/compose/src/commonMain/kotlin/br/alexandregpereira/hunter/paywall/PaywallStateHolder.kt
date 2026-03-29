package br.alexandregpereira.hunter.paywall

import br.alexandregpereira.hunter.event.v2.EventDispatcher
import br.alexandregpereira.hunter.event.v2.EventListener
import br.alexandregpereira.hunter.localization.AppLocalization
import br.alexandregpereira.hunter.paywall.domain.PaywallSettings
import br.alexandregpereira.hunter.paywall.domain.ShouldShowPaywall
import br.alexandregpereira.hunter.paywall.event.PaywallEvent
import br.alexandregpereira.hunter.paywall.event.PaywallResult
import br.alexandregpereira.hunter.revenue.GetCurrentOffer
import br.alexandregpereira.hunter.revenue.OfferPeriod
import br.alexandregpereira.hunter.revenue.Purchase
import br.alexandregpereira.hunter.revenue.RestorePurchase
import br.alexandregpereira.hunter.state.UiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

internal class PaywallStateHolder(
    private val paywallEventListener: EventListener<PaywallEvent>,
    private val paywallResultDispatcher: EventDispatcher<PaywallResult>,
    private val shouldShowPaywall: ShouldShowPaywall,
    private val settings: PaywallSettings,
    private val purchase: Purchase,
    private val getCurrentOffer: GetCurrentOffer,
    private val restorePurchase: RestorePurchase,
    appLocalization: AppLocalization,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) : UiModel<PaywallState>(initialState = PaywallState(
    strings = appLocalization.getPaywallStrings())
) {

    private var loadOfferJob: Job? = null
    private var selectedOfferId: String = ""

    init {
        observeEvents()
    }

    fun onStart() {
        scope.launch {
            if (settings.getPaywallWasClosedFlag().not() && shouldShowPaywall()) {
                openPaywall()
            }
        }
    }

    fun onClose() {
        setState { copy(isOpen = false) }
        loadOfferJob?.cancel()
        scope.launch {
            settings.savePaywallWasClosedFlag(
                paywallWasClosed = state.value.offer is PaywallOfferState.Success
            )
        }
    }

    fun onSubscribe() {
        setState { copy(isLoading = true) }
        scope.launch {
            try {
                purchase(offerId = selectedOfferId)
                closeAndDispatchSubscribeResult()
            } catch (cause: Throwable) {
                setState { copy(isLoading = false) }
                if (cause is CancellationException) throw cause
                cause.printStackTrace()
            }
        }
    }

    fun onRestoreSubscription() {
        setState { copy(isLoading = true) }
        scope.launch {
            try {
                restorePurchase()
                closeAndDispatchSubscribeResult()
            } catch (cause: Throwable) {
                setState { copy(isLoading = false) }
                if (cause is CancellationException) throw cause
                cause.printStackTrace()
            }
        }
    }

    fun onTryAgainLoadOffer() {
        loadOffer()
    }

    private fun loadOffer() {
        setState { copy(isLoading = true) }
        loadOfferJob = scope.launch {
            try {
                val offer = withContext(dispatcher) { getCurrentOffer() }
                selectedOfferId = offer.id
                setState {
                    val subscriptionValue = offer.value
                    val subscriptionPeriod = when (offer.period) {
                        OfferPeriod.MONTHLY -> strings.offerPeriodMonthly
                        OfferPeriod.YEARLY -> strings.offerPeriodYearly
                        OfferPeriod.WEEKLY -> strings.offerPeriodWeekly
                    }
                    val subscriptionOfferFormatted = strings.offerFormattedPrice
                        .replace("{value}", subscriptionValue)
                        .replace("{period}", subscriptionPeriod)
                    copy(
                        isLoading = false,
                        offer = PaywallOfferState.Success(
                            subscriptionOfferFormatted = subscriptionOfferFormatted,
                        ),
                    )
                }
            } catch (cause: Throwable) {
                if (cause is CancellationException) throw cause
                cause.printStackTrace()
                setState {
                    copy(
                        isLoading = false,
                        offer = PaywallOfferState.UnknownError,
                    )
                }
            }
        }
    }

    private fun observeEvents() {
        paywallEventListener.events.onEach {
            openPaywall()
        }.launchIn(scope)
    }

    private fun openPaywall() {
        val features = defaultFeatures(strings = state.value.strings)
        setState { copy(isOpen = true, features = features) }
        loadOffer()
    }

    private fun closeAndDispatchSubscribeResult() {
        setState { copy(isOpen = false) }
        paywallResultDispatcher.dispatchEvent(PaywallResult.OnSubscribe)
    }

    private fun defaultFeatures(
        strings: PaywallStrings
    ): ImmutableList<PaywallFeatureState> = persistentListOf(
        PaywallFeatureState(
            name = strings.featureAccessToAllFeatures,
            isPremium = false,
        ),
        PaywallFeatureState(
            name = strings.featureNoAds,
            isPremium = true,
        ),
        PaywallFeatureState(
            name = strings.featureFutureExclusiveContent,
            isPremium = true,
        ),
    )
}
