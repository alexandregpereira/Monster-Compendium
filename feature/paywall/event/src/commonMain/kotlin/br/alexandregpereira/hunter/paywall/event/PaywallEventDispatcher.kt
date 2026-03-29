package br.alexandregpereira.hunter.paywall.event

import br.alexandregpereira.hunter.event.v2.EventDispatcher

class PaywallEventDispatcher : EventDispatcher<PaywallEvent> by EventDispatcher()

sealed class PaywallEvent {
    data object ShowPaywall : PaywallEvent()
}
