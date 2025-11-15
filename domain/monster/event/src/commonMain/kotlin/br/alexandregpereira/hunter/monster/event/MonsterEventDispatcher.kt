/*
 * Copyright (C) 2024 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.monster.event

import br.alexandregpereira.hunter.event.EventListener
import br.alexandregpereira.hunter.monster.event.MonsterEvent.OnVisibilityChanges
import br.alexandregpereira.hunter.monster.registration.event.MonsterRegistrationResult
import br.alexandregpereira.hunter.monster.registration.event.collectOnSaved
import br.alexandregpereira.hunter.sync.event.SyncEventListener
import br.alexandregpereira.hunter.sync.event.collectSyncFinishedEvents
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
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

    data class OnCompendiumChanges(val monsterIndex: String? = null) : MonsterEvent()
}

internal fun MonsterEventDispatcher(
    syncEventListener: SyncEventListener,
    monsterRegistrationEventListener: EventListener<MonsterRegistrationResult>,
): MonsterEventDispatcher = object : MonsterEventDispatcher {
    private val _events: MutableSharedFlow<MonsterEvent> = MutableSharedFlow(
        extraBufferCapacity = 10,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    override val events: Flow<MonsterEvent> = _events.asSharedFlow()

    private val scope = MainScope()

    init {
        syncEventListener.collectSyncFinishedEvents {
            dispatchEvent(MonsterEvent.OnCompendiumChanges())
        }.launchIn(scope)

        monsterRegistrationEventListener.collectOnSaved { index ->
            dispatchEvent(MonsterEvent.OnCompendiumChanges(index))
        }.launchIn(scope)
    }

    override fun dispatchEvent(event: MonsterEvent) {
        _events.tryEmit(event)
    }
}

fun MonsterEventDispatcher.collectOnVisibilityChanges(
    action: suspend (OnVisibilityChanges) -> Unit
): Flow<OnVisibilityChanges> {
    return events.mapNotNull { it as? OnVisibilityChanges }.onEach(action)
}

fun MonsterEventDispatcher.collectOnMonsterPageChanges(
    action: (MonsterEvent.OnMonsterPageChanges) -> Unit
): Flow<Unit> {
    return events.mapNotNull { it as? MonsterEvent.OnMonsterPageChanges }.map(action)
}

fun MonsterEventDispatcher.collectOnMonsterCompendiumChanges(
    action: (MonsterEvent.OnCompendiumChanges) -> Unit
): Flow<MonsterEvent.OnCompendiumChanges> {
    return events.mapNotNull { it as? MonsterEvent.OnCompendiumChanges }.onEach(action)
}

fun emptyMonsterEventDispatcher(): MonsterEventDispatcher {
    return object : MonsterEventDispatcher {
        override val events: Flow<MonsterEvent> = emptyFlow()
        override fun dispatchEvent(event: MonsterEvent) {}
    }
}
