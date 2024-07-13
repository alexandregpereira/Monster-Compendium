package br.alexandregpereira.hunter.monster.event

import br.alexandregpereira.hunter.monster.event.MonsterEvent.OnVisibilityChanges
import br.alexandregpereira.hunter.sync.event.SyncEventListener
import br.alexandregpereira.hunter.sync.event.collectSyncFinishedEvents
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

interface MonsterEventDispatcher {
    val events: Flow<MonsterEvent>
    fun dispatchEvent(event: MonsterEvent)
}

sealed class MonsterEvent {
    sealed class OnVisibilityChanges : MonsterEvent() {
        data class Show(
            val index: String,
            val indexes: List<String> = emptyList(),
            val enableMonsterPageChangesEventDispatch: Boolean = false
        ) : OnVisibilityChanges()

        data object Hide : OnVisibilityChanges()
    }

    data class OnMonsterPageChanges(
        val monsterIndex: String
    ) : MonsterEvent()

    data object OnCompendiumChanges : MonsterEvent()
}

internal fun MonsterEventDispatcher(
    syncEventListener: SyncEventListener,
): MonsterEventDispatcher = object : MonsterEventDispatcher {
    private val _events: MutableSharedFlow<MonsterEvent> = MutableSharedFlow(
        extraBufferCapacity = 10,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    override val events: Flow<MonsterEvent> = _events.asSharedFlow()

    private val scope = MainScope()

    init {
        syncEventListener.collectSyncFinishedEvents {
            dispatchEvent(MonsterEvent.OnCompendiumChanges)
        }.launchIn(scope)
    }

    override fun dispatchEvent(event: MonsterEvent) {
        _events.tryEmit(event)
    }
}

fun MonsterEventDispatcher.collectOnVisibilityChanges(
    action: suspend (OnVisibilityChanges) -> Unit
): Flow<OnVisibilityChanges> {
    return events.map { it as? OnVisibilityChanges }.filterNotNull().onEach(action)
}

fun MonsterEventDispatcher.collectOnMonsterPageChanges(
    action: (MonsterEvent.OnMonsterPageChanges) -> Unit
): Flow<Unit> {
    return events.map { it as? MonsterEvent.OnMonsterPageChanges }.filterNotNull().map(action)
}

fun MonsterEventDispatcher.collectOnMonsterCompendiumChanges(
    action: () -> Unit
): Flow<Unit> {
    return events.filter { it is MonsterEvent.OnCompendiumChanges }.map { action() }
}

fun emptyMonsterEventDispatcher(): MonsterEventDispatcher {
    return object : MonsterEventDispatcher {
        override val events: Flow<MonsterEvent> = emptyFlow()
        override fun dispatchEvent(event: MonsterEvent) {}
    }
}
