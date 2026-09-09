package br.alexandregpereira.hunter.paywall

import br.alexandregpereira.hunter.analytics.Analytics
import br.alexandregpereira.hunter.event.v2.EventDispatcher
import br.alexandregpereira.hunter.event.v2.EventListener
import br.alexandregpereira.hunter.localization.AppLocalization
import br.alexandregpereira.hunter.paywall.domain.PaywallCooldown
import br.alexandregpereira.hunter.paywall.domain.ShouldShowPaywall
import br.alexandregpereira.hunter.paywall.event.PaywallEvent
import br.alexandregpereira.hunter.paywall.event.PaywallResult
import br.alexandregpereira.hunter.revenue.GetCurrentOffer
import br.alexandregpereira.hunter.revenue.IsPremium
import br.alexandregpereira.hunter.revenue.OfferPeriod
import br.alexandregpereira.hunter.revenue.Purchase
import br.alexandregpereira.hunter.revenue.RestorePurchase
import br.alexandregpereira.hunter.state.MutableActionHandler
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
    private val isPremium: IsPremium,
    private val cooldown: PaywallCooldown,
    private val purchase: Purchase,
    private val getCurrentOffer: GetCurrentOffer,
    private val restorePurchase: RestorePurchase,
    private val appLocalization: AppLocalization,
    private val analytics: Analytics,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) : UiModel<PaywallState>(initialState = PaywallState()),
    MutableActionHandler<PaywallViewAction> by MutableActionHandler() {

    private var loadOfferJob: Job? = null
    private var selectedOfferId: String = ""

    /**
     * Only an automatic open feeds the cooldown. Closing a paywall the user opened on purpose,
     * from the promo banner or the settings menu, must not hide the automatic one.
     */
    private var openedAutomatically: Boolean = false

    init {
        observeEvents()
    }

    fun onStart() {
        if (state.value.isOpen) {
            return
        }
        scope.launch {
            if (shouldShowPaywall()) {
                openPaywall(automatic = true)
            }
        }
    }

    fun onResume() {
        if (state.value.isOpen) {
            return
        }
        onStart()
    }

    fun onClose() {
        val actionResultState = state.value.actionResultState
        closePaywall()
        loadOfferJob?.cancel()
        loadOfferJob = null
        if (openedAutomatically.not()) {
            return
        }
        // A purchase or restore failure is the highest intent signal there is, so those users get
        // another chance on the next session instead of starting the cooldown.
        val shouldRegisterDismissal = actionResultState == null ||
                actionResultState is PaywallActionResultState.GetCurrentOfferError
        if (shouldRegisterDismissal.not()) {
            return
        }
        scope.launch { cooldown.registerDismissal() }
    }

    fun onSubscribe() {
        analytics.track(eventName = "Paywall - subscribe clicked")
        setState { copy(isLoading = true, actionResultState = null) }
        scope.launch {
            try {
                purchase(offerId = selectedOfferId)
                analytics.track(eventName = "Paywall - subscribed")
                changeToSuccessState()
            } catch (cause: Throwable) {
                if (cause is CancellationException){
                    setState { copy(isLoading = false) }
                    throw cause
                }
                analytics.logException(cause)
                analytics.track(eventName = "Paywall - subscribe error")
                setState { copy(isLoading = false, actionResultState = PaywallActionResultState.PurchaseError) }
            }
        }
    }

    fun onRestoreSubscription() {
        analytics.track(eventName = "Paywall - restore purchase clicked")
        setState { copy(isLoading = true, actionResultState = null) }
        scope.launch {
            try {
                restorePurchase()
                analytics.track(eventName = "Paywall - subscription restored")
                changeToSuccessState()
            } catch (cause: Throwable) {
                if (cause is CancellationException) {
                    setState { copy(isLoading = false) }
                    throw cause
                }
                analytics.logException(cause)
                analytics.track(eventName = "Paywall - restore purchase error")
                setState { copy(isLoading = false, actionResultState = PaywallActionResultState.RestorePurchaseError) }
            }
        }
    }

    fun onTryAgainLoadOffer() {
        analytics.track(eventName = "Paywall - try again load offer clicked")
        loadOffer()
    }

    fun onTryAgainPurchase() {
        analytics.track(eventName = "Paywall - try again purchase clicked")
        onSubscribe()
    }

    fun onComeBackToOffer() {
        analytics.track(eventName = "Paywall - come back to offer clicked")
        setState { copy(actionResultState = null) }
    }

    fun onTermsClick() {
        analytics.track(eventName = "Paywall - terms clicked")
        sendAction(PaywallViewAction.GoToUrl(state.value.strings.termsUrl))
    }

    fun onPrivacyClick() {
        analytics.track(eventName = "Paywall - privacy clicked")
        sendAction(PaywallViewAction.GoToUrl(state.value.strings.privacyUrl))
    }

    private fun loadOffer() {
        setState { copy(isLoading = true) }
        loadOfferJob?.cancel()
        loadOfferJob = scope.launch {
            try {
                val isPremium = withContext(dispatcher) { isPremium(ignoreCache = true) }
                if (isPremium) {
                    changeToSuccessState()
                    return@launch
                }
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
                        actionResultState = null,
                        subscriptionOfferFormatted = subscriptionOfferFormatted,
                    )
                }
                analytics.track(eventName = "Paywall - offer loaded")
            } catch (cause: Throwable) {
                if (cause is CancellationException) {
                    setState { copy(isLoading = false) }
                    throw cause
                }
                analytics.logException(cause)
                analytics.track(eventName = "Paywall - load offer error")
                setState {
                    copy(
                        isLoading = false,
                        actionResultState = PaywallActionResultState.GetCurrentOfferError,
                    )
                }
            }
        }
    }

    private fun observeEvents() {
        paywallEventListener.events.onEach {
            openPaywall(automatic = false)
        }.launchIn(scope)
    }

    private fun openPaywall(automatic: Boolean) {
        openedAutomatically = automatic
        analytics.track(
            eventName = "Paywall - opened",
            params = mapOf("automatic" to automatic),
        )
        val localizedStrings = appLocalization.getPaywallStrings()
        val features = defaultFeatures(strings = localizedStrings)
        setState {
            copy(
                isOpen = true,
                features = features,
                actionResultState = null,
                strings = localizedStrings,
                isLoading = true,
            )
        }
        loadOffer()
    }

    private fun closePaywall() {
        analytics.track(eventName = "Paywall - closed")
        setState { copy(isOpen = false) }
    }

    private fun changeToSuccessState() {
        paywallResultDispatcher.dispatchEvent(PaywallResult.OnSubscribe)
        setState { copy(isLoading = false, actionResultState = PaywallActionResultState.Success) }
        scope.launch { cooldown.reset() }
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
    )
}
