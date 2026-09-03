package br.alexandregpereira.hunter.ads

import br.alexandregpereira.hunter.analytics.Analytics
import br.alexandregpereira.hunter.event.v2.EventDispatcher
import br.alexandregpereira.hunter.event.v2.EventListener
import br.alexandregpereira.hunter.localization.AppLocalization
import br.alexandregpereira.hunter.paywall.event.PaywallEvent
import br.alexandregpereira.hunter.paywall.event.PaywallResult
import br.alexandregpereira.hunter.revenue.IsPremium
import br.alexandregpereira.hunter.state.UiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

internal class AdsStateHolder(
    private val isPremium: IsPremium,
    private val paywallResultListener: EventListener<PaywallResult>,
    private val paywallEventDispatcher: EventDispatcher<PaywallEvent>,
    private val appLocalization: AppLocalization,
    private val analytics: Analytics,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) : UiModel<AdsState>(AdsState()) {

    private var promoBannerJob: Job? = null
    private var promoBannerTracked: Boolean = false

    fun onStart() {
        checkUsageLimit(trackBannerView = true)
        observeSubscriptionResults()
        startPromoBannerWindow()
    }

    fun checkUsageLimit(trackBannerView: Boolean = false) {
        flow {
            emit(isPremium())
        }.flowOn(dispatcher)
            .onEach { isPremium ->
                if (isPremium.not() && trackBannerView) {
                    analytics.track(eventName = "Ads - banner viewed")
                }
                setState {
                    copy(
                        isVisible = isPremium.not(),
                        strings = appLocalization.getAdsStrings(),
                    )
                }
            }
            .launchIn(scope)
    }

    /**
     * The ad banner is only composed after the promo banner has been on screen for
     * [PROMO_BANNER_MIN_DURATION_IN_MILLIS]. Both banners share the same slot and are never
     * rendered at the same time, so the ad is never covered by the promo. When the ad fails to
     * load, which is what happens when the user has an ad blocker, the promo banner comes back
     * and the ad is requested again only after [PROMO_BANNER_RETRY_DURATION_IN_MILLIS].
     */
    fun onAdFailedToLoad() {
        analytics.track(eventName = "Ads - banner load failed")
        startPromoBannerWindow(durationInMillis = PROMO_BANNER_RETRY_DURATION_IN_MILLIS)
    }

    fun onAdLoaded() {
        analytics.track(eventName = "Ads - banner loaded")
    }

    fun onPromoBannerClick() {
        analytics.track(eventName = "Ads - promo banner clicked")
        paywallEventDispatcher.dispatchEvent(PaywallEvent.ShowPaywall)
    }

    private fun startPromoBannerWindow(
        durationInMillis: Long = PROMO_BANNER_MIN_DURATION_IN_MILLIS,
    ) {
        promoBannerJob?.cancel()
        promoBannerJob = scope.launch {
            setState { copy(isAdSlotReady = false) }
            if (promoBannerTracked.not()) {
                promoBannerTracked = true
                analytics.track(eventName = "Ads - promo banner viewed")
            }
            delay(durationInMillis)
            setState { copy(isAdSlotReady = true) }
        }
    }

    private fun observeSubscriptionResults() {
        paywallResultListener.events
            .onEach { result ->
                when (result) {
                    PaywallResult.OnSubscribe -> {
                        analytics.track(eventName = "Ads - banner closed")
                        promoBannerJob?.cancel()
                        setState { copy(isVisible = false) }
                    }
                }
            }
            .launchIn(scope)
    }
}

private const val PROMO_BANNER_MIN_DURATION_IN_MILLIS = 15_000L
private const val PROMO_BANNER_RETRY_DURATION_IN_MILLIS = 60_000L
