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

package br.alexandregpereira.hunter.monster.registration.event

import br.alexandregpereira.hunter.event.EventDispatcher
import br.alexandregpereira.hunter.event.EventListener
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map

sealed class MonsterRegistrationEvent {

    data class ShowEdit(val monsterIndex: String) : MonsterRegistrationEvent()

    data object Hide : MonsterRegistrationEvent()
}

sealed class MonsterRegistrationResult {
    data class OnSaved(val monsterIndex: String) : MonsterRegistrationResult()
}

interface MonsterRegistrationEventListener : EventListener<MonsterRegistrationResult>

interface MonsterRegistrationEventDispatcher : EventDispatcher<MonsterRegistrationEvent>

fun EventListener<MonsterRegistrationResult>.collectOnSaved(
    onAction: (String) -> Unit
): Flow<Unit> = events.map { it as? MonsterRegistrationResult.OnSaved }
    .map { event -> event?.let { onAction(it.monsterIndex) } }

fun emptyMonsterRegistrationEventDispatcher(): MonsterRegistrationEventDispatcher {
    return object : MonsterRegistrationEventDispatcher {
        override fun dispatchEvent(event: MonsterRegistrationEvent) {}
    }
}

fun emptyMonsterRegistrationEventListener(): MonsterRegistrationEventListener {
    return object : MonsterRegistrationEventListener {
        override val events: Flow<MonsterRegistrationResult> = emptyFlow()
    }
}