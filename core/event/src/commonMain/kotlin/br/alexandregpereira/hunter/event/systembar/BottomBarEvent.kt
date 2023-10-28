package br.alexandregpereira.hunter.event.systembar

import br.alexandregpereira.hunter.event.EventDispatcher
import br.alexandregpereira.hunter.event.EventManager

class BottomBarEventManager : EventManager<BottomBarEvent> by EventManager()

sealed class BottomBarEvent {

    data class AddTopContent(val topContentId: String) : BottomBarEvent()

    data class RemoveTopContent(val topContentId: String) : BottomBarEvent()
}

fun EventDispatcher<BottomBarEvent>.dispatchAddTopContentEvent(topContentId: String) {
    dispatchEvent(BottomBarEvent.AddTopContent(topContentId))
}

fun EventDispatcher<BottomBarEvent>.dispatchRemoveTopContentEvent(topContentId: String) {
    dispatchEvent(BottomBarEvent.RemoveTopContent(topContentId))
}
