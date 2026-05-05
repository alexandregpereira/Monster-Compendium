package br.alexandregpereira.hunter.ads

import br.alexandregpereira.hunter.analytics.Analytics
import br.alexandregpereira.hunter.event.v2.EventListener
import br.alexandregpereira.hunter.paywall.event.PaywallResult
import br.alexandregpereira.hunter.revenue.IsSessionUsageLimitReached
import br.alexandregpereira.hunter.state.UiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal class AdsStateHolder(
    private val isSessionUsageLimitReached: IsSessionUsageLimitReached,
    private val paywallResultListener: EventListener<PaywallResult>,
    private val analytics: Analytics,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) : UiModel<AdsState>(AdsState()) {

    fun onStart() {
        checkUsageLimit(trackBannerView = true)
        observeSubscriptionResults()
    }

    fun checkUsageLimit(trackBannerView: Boolean = false) {
        flow {
            emit(isSessionUsageLimitReached())
        }.flowOn(dispatcher)
            .onEach { isLimitReached ->
                if (isLimitReached && trackBannerView) {
                    analytics.track(eventName = "Ads - banner viewed")
                }
                setState { copy(isVisible = isLimitReached) }
            }
            .launchIn(scope)
    }

    private fun observeSubscriptionResults() {
        paywallResultListener.events
            .onEach { result ->
                when (result) {
                    PaywallResult.OnSubscribe -> {
                        analytics.track(eventName = "Ads - banner closed")
                        setState { copy(isVisible = false) }
                    }
                }
            }
            .launchIn(scope)
    }
}
