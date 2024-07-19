package br.alexadregpereira.hunter.shareContent.event

import br.alexandregpereira.hunter.event.v2.EventDispatcher
import br.alexandregpereira.hunter.event.v2.EventListener
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance

class ShareContentEventDispatcher : EventDispatcher<ShareContentEvent> by EventDispatcher()

sealed class ShareContentEvent {
    sealed class Import : ShareContentEvent() {
        data object OnStart : Import()
        data object OnFinish : Import()
    }
    sealed class Export : ShareContentEvent() {
        data class OnStart(val monsterIndex: String) : Export()
        data object OnFinish : Export()
    }
}

fun EventListener<ShareContentEvent>.importEvents(): Flow<ShareContentEvent.Import> {
    return events.filterIsInstance()
}

fun EventListener<ShareContentEvent>.exportEvents(): Flow<ShareContentEvent.Export> {
    return events.filterIsInstance()
}
