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

import br.alexandregpereira.hunter.event.EventDispatcher
import br.alexandregpereira.hunter.event.EventListener
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map

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

interface SpellRegistrationEventListener : EventListener<SpellRegistrationResult>

interface SpellRegistrationEventDispatcher : EventDispatcher<SpellRegistrationEvent>

fun EventListener<SpellRegistrationResult>.collectOnSaved(
    onAction: (String) -> Unit
): Flow<Unit> = events.map { it as? SpellRegistrationResult.OnSaved }
    .map { event -> event?.let { onAction(it.spellIndex) } }

fun emptySpellRegistrationEventDispatcher(): SpellRegistrationEventDispatcher {
    return object : SpellRegistrationEventDispatcher {
        override fun dispatchEvent(event: SpellRegistrationEvent) {}
    }
}

fun emptySpellRegistrationEventListener(): SpellRegistrationEventListener {
    return object : SpellRegistrationEventListener {
        override val events: Flow<SpellRegistrationResult> = emptyFlow()
    }
}
