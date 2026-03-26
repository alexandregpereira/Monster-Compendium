package br.alexandregpereira.hunter.revenue

import br.alexandregpereira.hunter.event.v2.EventListener
import br.alexandregpereira.hunter.revenue.event.RevenueEvent
import br.alexandregpereira.hunter.state.UiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class PaywallStateHolder(
    private val revenueEventListener: EventListener<RevenueEvent>,
    private val isSessionUsageLimitReached: IsSessionUsageLimitReached,
    private val subscribe: Subscribe,
) : UiModel<PaywallState>(initialState = PaywallState()) {

    init {
        observeEvents()
    }

    fun onStart() {
        scope.launch {
            if (isSessionUsageLimitReached()) {
                setState { copy(isOpen = true) }
            }
        }
    }

    fun onClosePaywall() {
        setState { copy(isOpen = false) }
    }

    fun onSubscribe() {
        scope.launch {
            subscribe()
            withContext(Dispatchers.Main) {
                setState { copy(isOpen = false) }
            }
        }
    }

    private fun observeEvents() {
        revenueEventListener.events.onEach {
            setState { copy(isOpen = true) }
        }.launchIn(scope)
    }
}
