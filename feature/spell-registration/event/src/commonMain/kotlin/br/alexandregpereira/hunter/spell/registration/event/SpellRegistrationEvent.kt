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

package br.alexandregpereira.hunter.spell.registration.event

import br.alexandregpereira.hunter.event.v2.EventDispatcher
import br.alexandregpereira.hunter.event.v2.EventListener
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

sealed class SpellRegistrationEvent {

    /**
     * Open the spell registration form.
     * If [spellIndex] is empty, a new spell will be created.
     * If [spellIndex] is non-empty, the existing spell will be edited.
     */
    data class Show(val spellIndex: String = "") : SpellRegistrationEvent()

    data object Hide : SpellRegistrationEvent()
}

sealed class SpellRegistrationResult {
    data class OnSaved(val spellIndex: String) : SpellRegistrationResult()
}

class SpellRegistrationEventDispatcher : EventDispatcher<SpellRegistrationEvent> by EventDispatcher(
    extraBufferCapacity = 1,
)

class SpellRegistrationResultDispatcher : EventDispatcher<SpellRegistrationResult> by EventDispatcher(
    extraBufferCapacity = 1,
)

fun EventListener<SpellRegistrationResult>.collectOnSaved(
    onAction: suspend (SpellRegistrationResult.OnSaved) -> Unit
): Flow<SpellRegistrationResult.OnSaved> = events.map { it as? SpellRegistrationResult.OnSaved }
    .filterNotNull().onEach(onAction)
