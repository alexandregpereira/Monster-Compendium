package br.alexandregpereira.hunter.revenue

import br.alexandregpereira.hunter.event.v2.EventListener
import br.alexandregpereira.hunter.revenue.event.RevenueEvent
import br.alexandregpereira.hunter.state.UiModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

internal class PaywallStateHolder(
    private val revenueEventListener: EventListener<RevenueEvent>,
    private val isSessionUsageLimitReached: IsSessionUsageLimitReached,
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

    fun onDismiss() {
        setState { copy(isOpen = false) }
    }

    fun onPurchaseCompleted() {
        setState { copy(isOpen = false) }
    }

    fun onPurchaseError(message: String) {
        // Optionally log or show error message
        // For now, we just keep the paywall open so the user can try again
    }

    private fun observeEvents() {
        revenueEventListener.events.onEach {
            setState { copy(isOpen = true) }
        }.launchIn(scope)
    }
}
