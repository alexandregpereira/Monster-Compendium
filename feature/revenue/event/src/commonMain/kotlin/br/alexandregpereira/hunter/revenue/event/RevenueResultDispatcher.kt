package br.alexandregpereira.hunter.revenue.event

import br.alexandregpereira.hunter.event.v2.EventDispatcher

class RevenueResultDispatcher : EventDispatcher<RevenueResult> by EventDispatcher()

sealed class RevenueResult {
    data object OnSubscribe : RevenueResult()
}
