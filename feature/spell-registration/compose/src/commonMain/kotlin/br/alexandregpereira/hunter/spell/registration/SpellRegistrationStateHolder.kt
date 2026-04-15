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

package br.alexandregpereira.hunter.spell.registration

import br.alexandregpereira.hunter.analytics.Analytics
import br.alexandregpereira.hunter.domain.spell.GetSpellUseCase
import br.alexandregpereira.hunter.domain.spell.SaveSpells
import br.alexandregpereira.hunter.domain.spell.model.Spell
import br.alexandregpereira.hunter.domain.spell.model.SpellStatus
import br.alexandregpereira.hunter.event.EventManager
import br.alexandregpereira.hunter.localization.AppLocalization
import br.alexandregpereira.hunter.spell.registration.event.SpellRegistrationEvent
import br.alexandregpereira.hunter.spell.registration.event.SpellRegistrationResult
import br.alexandregpereira.hunter.state.UiModel
import br.alexandregpereira.hunter.uuid.generateUUID
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

internal class SpellRegistrationStateHolder(
    private val dispatcher: CoroutineDispatcher,
    private val getSpell: GetSpellUseCase,
    private val saveSpells: SaveSpells,
    private val eventManager: EventManager<SpellRegistrationEvent>,
    private val resultManager: EventManager<SpellRegistrationResult>,
    private val appLocalization: AppLocalization,
    private val analytics: Analytics,
) : UiModel<SpellRegistrationState>(SpellRegistrationState()) {

    fun observeEvents() {
        eventManager.events
            .onEach { event ->
                when (event) {
                    is SpellRegistrationEvent.Show -> {
                        analytics.track(
                            eventName = "Spell Registration - opened",
                            params = mapOf("index" to event.spellIndex),
                        )
                        val strings = getSpellRegistrationStrings(appLocalization.getLanguage())
                        if (event.spellIndex.isNotEmpty()) {
                            loadSpell(event.spellIndex, strings)
                        } else {
                            val newIndex = generateUUID()
                            setState {
                                copy(
                                    isOpen = true,
                                    isLoading = false,
                                    isEditing = false,
                                    isSaveEnabled = false,
                                    spell = SpellFormState(index = newIndex),
                                    strings = strings,
                                )
                            }
                        }
                    }
                    is SpellRegistrationEvent.Hide -> {
                        analytics.track(eventName = "Spell Registration - closed")
                        setState { copy(isOpen = false) }
                    }
                }
            }
            .launchIn(scope)
    }

    private fun loadSpell(spellIndex: String, strings: SpellRegistrationStrings) {
        setState { copy(isOpen = true, isLoading = true, strings = strings) }
        getSpell(spellIndex)
            .map { spell -> spell.asFormState() }
            .flowOn(dispatcher)
            .onEach { formState ->
                analytics.track(
                    eventName = "Spell Registration - loaded",
                    params = mapOf("index" to spellIndex),
                )
                setState {
                    copy(
                        isLoading = false,
                        isEditing = true,
                        isSaveEnabled = false,
                        spell = formState,
                    )
                }
            }
            .catch { setState { copy(isLoading = false) } }
            .launchIn(scope)
    }

    fun onSpellChanged(spell: SpellFormState) {
        setState { copy(spell = spell, isSaveEnabled = true) }
    }

    fun onSave() {
        analytics.track(eventName = "Spell Registration - saved")
        val spellIndex = state.value.spell.index
        saveSpells(listOf(state.value.spell.asDomain()))
            .flowOn(dispatcher)
            .onEach {
                onClose()
                resultManager.dispatchEvent(SpellRegistrationResult.OnSaved(spellIndex))
            }
            .catch { }
            .launchIn(scope)
    }

    fun onClose() {
        eventManager.dispatchEvent(SpellRegistrationEvent.Hide)
    }
}

private fun Spell.asFormState(): SpellFormState = SpellFormState(
    index = index,
    name = name,
    level = level,
    castingTime = castingTime,
    components = components,
    duration = duration,
    range = range,
    ritual = ritual,
    concentration = concentration,
    savingThrowType = savingThrowType,
    damageType = damageType.orEmpty(),
    school = school,
    description = description,
    higherLevel = higherLevel.orEmpty(),
)

private fun SpellFormState.asDomain(): Spell = Spell(
    index = index,
    name = name,
    level = level,
    castingTime = castingTime,
    components = components,
    duration = duration,
    range = range,
    ritual = ritual,
    concentration = concentration,
    savingThrowType = savingThrowType,
    damageType = damageType.takeIf { it.isNotBlank() },
    school = school,
    description = description,
    higherLevel = higherLevel.takeIf { it.isNotBlank() },
    status = SpellStatus.Imported,
)
