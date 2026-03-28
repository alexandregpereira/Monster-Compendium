package br.alexandregpereira.hunter.revenue

import br.alexandregpereira.hunter.event.v2.EventDispatcher
import br.alexandregpereira.hunter.event.v2.EventListener
import br.alexandregpereira.hunter.revenue.domain.PaywallSettings
import br.alexandregpereira.hunter.revenue.domain.ShouldShowPaywall
import br.alexandregpereira.hunter.revenue.event.RevenueEvent
import br.alexandregpereira.hunter.revenue.event.RevenueResult
import br.alexandregpereira.hunter.state.UiModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

internal class PaywallStateHolder(
    private val revenueEventListener: EventListener<RevenueEvent>,
    private val revenueResultDispatcher: EventDispatcher<RevenueResult>,
    private val shouldShowPaywall: ShouldShowPaywall,
    private val settings: PaywallSettings,
    private val subscribe: Subscribe,
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

    fun onSubscribe() {
        flow {
            emit(subscribe())
        }.catch { cause ->
            cause.printStackTrace()
        }.onEach {
            setState { copy(isOpen = false) }
            revenueResultDispatcher.dispatchEvent(RevenueResult.OnSubscribe)
        }.launchIn(scope)
    }

    fun onRestoreSubscription() {

    }

    private fun observeEvents() {
        revenueEventListener.events.onEach {
            setState { copy(isOpen = true) }
        }.launchIn(scope)
    }
}
