package br.alexandregpereira.hunter.revenue

import br.alexandregpereira.hunter.event.v2.EventDispatcher
import br.alexandregpereira.hunter.event.v2.EventListener
import br.alexandregpereira.hunter.revenue.domain.PaywallSettings
import br.alexandregpereira.hunter.revenue.domain.ShouldShowPaywall
import br.alexandregpereira.hunter.revenue.event.RevenueEvent
import br.alexandregpereira.hunter.revenue.event.RevenueResult
import br.alexandregpereira.hunter.state.UiModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

internal class PaywallStateHolder(
    private val revenueEventListener: EventListener<RevenueEvent>,
    private val shouldShowPaywall: ShouldShowPaywall,
    private val revenueResultDispatcher: EventDispatcher<RevenueResult>,
    private val settings: PaywallSettings,
) : UiModel<PaywallState>(initialState = PaywallState()) {

    init {
        observeEvents()
    }

    fun onStart() {
        scope.launch {
            if (settings.getPaywallWasClosedFlag().not() && shouldShowPaywall()) {
                setState { copy(isOpen = true) }
            }
        }
    }

    fun onClose() {
        setState { copy(isOpen = false) }
        scope.launch {
            settings.savePaywallWasClosedFlag(paywallWasClosed = true)
        }
    }

    fun onPurchaseCompleted() {
        setState { copy(isOpen = false) }
        revenueResultDispatcher.dispatchEvent(RevenueResult.OnSubscribe)
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
