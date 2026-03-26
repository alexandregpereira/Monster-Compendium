package br.alexandregpereira.hunter.revenue.event

import br.alexandregpereira.hunter.event.v2.EventDispatcher

class RevenueEventDispatcher : EventDispatcher<RevenueEvent> by EventDispatcher()

sealed class RevenueEvent {
    data object ShowPaywall : RevenueEvent()
}
