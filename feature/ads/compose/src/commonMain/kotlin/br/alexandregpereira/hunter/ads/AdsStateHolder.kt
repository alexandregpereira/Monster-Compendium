package br.alexandregpereira.hunter.ads

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
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) : UiModel<AdsState>(AdsState()) {

    fun onStart() {
        checkUsageLimit()
        observeSubscriptionResults()
    }

    private fun checkUsageLimit() {
        flow {
            emit(isSessionUsageLimitReached())
        }.flowOn(dispatcher)
            .onEach { isLimitReached ->
                setState { copy(isVisible = isLimitReached) }
            }
            .launchIn(scope)
    }

    private fun observeSubscriptionResults() {
        paywallResultListener.events
            .onEach { result ->
                when (result) {
                    PaywallResult.OnSubscribe -> setState { copy(isVisible = false) }
                }
            }
            .launchIn(scope)
    }
}
