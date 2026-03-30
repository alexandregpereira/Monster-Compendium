package br.alexandregpereira.hunter.paywall.event

import br.alexandregpereira.hunter.event.v2.EventDispatcher

class PaywallResultDispatcher : EventDispatcher<PaywallResult> by EventDispatcher(
    extraBufferCapacity = 1,
)

sealed class PaywallResult {
    data object OnSubscribe : PaywallResult()
}
