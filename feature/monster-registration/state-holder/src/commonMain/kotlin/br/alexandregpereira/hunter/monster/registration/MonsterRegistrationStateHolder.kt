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

package br.alexandregpereira.hunter.monster.registration

import br.alexandregpereira.hunter.analytics.Analytics
import br.alexandregpereira.hunter.domain.model.AbilityScoreType
import br.alexandregpereira.hunter.domain.model.ConditionType
import br.alexandregpereira.hunter.domain.model.DamageType
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.monster.lore.GetMonsterLoreUseCase
import br.alexandregpereira.hunter.domain.monster.lore.SaveMonstersLoreUseCase
import br.alexandregpereira.hunter.domain.monster.lore.model.MonsterLore
import br.alexandregpereira.hunter.domain.monster.lore.model.MonsterLoreEntry
import br.alexandregpereira.hunter.domain.monster.spell.model.SchoolOfMagic
import br.alexandregpereira.hunter.domain.spell.GetSpellUseCase
import br.alexandregpereira.hunter.domain.usecase.GetMonsterUseCase
import br.alexandregpereira.hunter.event.EventManager
import br.alexandregpereira.hunter.localization.AppLocalization
import br.alexandregpereira.hunter.monster.registration.domain.NormalizeMonsterUseCase
import br.alexandregpereira.hunter.monster.registration.domain.SaveMonsterUseCase
import br.alexandregpereira.hunter.monster.registration.domain.filterEmpties
import br.alexandregpereira.hunter.monster.registration.event.MonsterRegistrationEvent
import br.alexandregpereira.hunter.monster.registration.event.MonsterRegistrationResult
import br.alexandregpereira.hunter.monster.registration.mapper.asDomain
import br.alexandregpereira.hunter.monster.registration.mapper.asState
import br.alexandregpereira.hunter.monster.registration.mapper.editBy
import br.alexandregpereira.hunter.monster.registration.mapper.name
import br.alexandregpereira.hunter.spell.compendium.event.SpellCompendiumEvent
import br.alexandregpereira.hunter.spell.compendium.event.SpellCompendiumEvent.Show
import br.alexandregpereira.hunter.spell.compendium.event.SpellCompendiumEventResultDispatcher
import br.alexandregpereira.hunter.spell.compendium.event.SpellCompendiumResult
import br.alexandregpereira.hunter.spell.detail.event.SpellDetailEvent
import br.alexandregpereira.hunter.spell.detail.event.SpellDetailEventDispatcher
import br.alexandregpereira.hunter.state.MutableActionHandler
import br.alexandregpereira.hunter.state.StateHolderParams
import br.alexandregpereira.hunter.state.UiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class MonsterRegistrationStateHolder internal constructor(
    private val dispatcher: CoroutineDispatcher,
    private val params: StateHolderParams<MonsterRegistrationParams>,
    private val eventManager: EventManager<MonsterRegistrationEvent>,
    private val eventResultManager: EventManager<MonsterRegistrationResult>,
    private val getMonster: GetMonsterUseCase,
    private val getMonsterLore: GetMonsterLoreUseCase,
    private val saveMonster: SaveMonsterUseCase,
    private val normalizeMonster: NormalizeMonsterUseCase,
    private val analytics: Analytics,
    private val spellCompendiumEventDispatcher: SpellCompendiumEventResultDispatcher,
    private val spellDetailEventDispatcher: SpellDetailEventDispatcher,
    private val getSpell: GetSpellUseCase,
    private val appLocalization: AppLocalization,
) : UiModel<MonsterRegistrationState>(MonsterRegistrationState()),
    MutableActionHandler<MonsterRegistrationAction> by MutableActionHandler(),
    MonsterRegistrationIntent {

    private var originalMonster: Monster? = null
    private var originalMonsterLore: MonsterLore? = null
    private var metadata: Metadata = Metadata()

    init {
        observeEvents()
        if (state.value.isOpen) {
            fetchMonster()
        }
    }

    override fun onClose() {
        eventManager.dispatchEvent(MonsterRegistrationEvent.Hide)
    }

    override fun onMonsterChanged(monster: MonsterState) {
        val newMonster = metadata.monster.editBy(monster, appLocalization.getStrings())
        val newMonsterLoreEntries = monster.loreEntries.map { it.asDomain() }
        setMetadata(newMonster, newMonsterLoreEntries)
        updateMonster()
    }

    override fun onSaved() {
        val monsterLoreEntries = metadata.monsterLoreEntries
        val originalMonster = originalMonster
        val originalMonsterLore = originalMonsterLore
        normalizeMonster(metadata.monster)
            .map { monster ->
                analytics.trackMonsterRegistrationSaved(monster)
                saveMonster(monster, originalMonster, monsterLoreEntries, originalMonsterLore)
                monster
            }
            .flowOn(dispatcher)
            .onEach {
                onClose()
                eventResultManager.dispatchEvent(
                    MonsterRegistrationResult.OnSaved(
                        monsterIndex = state.value.monster.index
                    )
                )
            }
            .launchIn(scope)
    }

    override fun onSpellClick(spellIndex: String) {
        analytics.trackMonsterRegistrationSpellClicked(spellIndex)
        val showEvent = Show(
            spellIndex = spellIndex,
            selectedSpellIndexes = state.value.monster.spellcastings
                .flatMap { it.spellsByGroup }
                .flatMap { it.spells }
                .map { it.index }
        )
        spellCompendiumEventDispatcher.dispatchEventResult(showEvent).onEach { result ->
            when (result) {
                is SpellCompendiumResult.OnSpellClick -> updateSpells(
                    currentSpellIndex = spellIndex,
                    newSpellIndex = result.spellIndex
                )
                is SpellCompendiumResult.OnSpellLongClick -> openSpellDetail(result.spellIndex)
            }
        }.launchIn(scope)
    }

    override fun onTableContentClick(key: String) {
        analytics.trackMonsterRegistrationTableContentClicked(key)
        onTableContentClose()
        scope.launch {
            delay(300)
            sendAction(
                MonsterRegistrationAction.GoToListPosition(state.value.monster.keysList.indexOf(key))
            )
        }
    }

    override fun onTableContentClose() {
        analytics.onTableContentClosed()
        setState { copy(isTableContentOpen = false)}
    }

    override fun onTableContentOpen() {
        analytics.onTableContentOpened()
        setState { copy(isTableContentOpen = true)}
    }

    private fun updateSpells(currentSpellIndex: String, newSpellIndex: String) {
        spellCompendiumEventDispatcher.dispatchEventResult(SpellCompendiumEvent.Hide)
        getSpell(newSpellIndex)
            .flowOn(dispatcher)
            .onEach { newSpell ->
                val newMonster = metadata.monster.copy(
                    spellcastings = metadata.monster.spellcastings.map { spellcasting ->
                        spellcasting.copy(
                            usages = spellcasting.usages.map { usage ->
                                usage.copy(
                                    spells = usage.spells.map { spell ->
                                        if (spell.index == currentSpellIndex) {
                                            spell.copy(
                                                index = newSpellIndex,
                                                name = newSpell.name,
                                                level = newSpell.level,
                                                school = SchoolOfMagic.valueOf(newSpell.school.name),
                                            )
                                        } else {
                                            spell
                                        }
                                    }
                                )
                            }
                        )
                    }
                )
                metadata = metadata.copy(monster = newMonster)
                updateMonster()
            }
            .launchIn(scope)
    }

    private fun updateMonster() {
        setState {
            val strings = appLocalization.getStrings()
            val monsterState = metadata.asState(strings)
            copy(
                monster = monsterState,
                isSaveButtonEnabled = metadata.monster.filterEmpties() != originalMonster
                        || metadata.monsterLoreEntries != originalMonsterLore?.entries.orEmpty(),
                isLoading = false,
                tableContent = SectionTitle.entries.associate { it.name to it.name(strings) },
            )
        }
    }

    private fun openSpellDetail(spellIndex: String) {
        spellDetailEventDispatcher.dispatchEvent(SpellDetailEvent.ShowSpell(spellIndex))
    }

    private fun fetchMonster() {
        val monsterIndex = params.value.monsterIndex?.takeUnless { it.isBlank() } ?: return

        setState { copy(isLoading = true) }
        getMonster(monsterIndex)
            .map { monster ->
                monster to getMonsterLore(monster.index).single()
            }
            .flowOn(dispatcher)
            .onEach { (monster, monsterLore) ->
                originalMonster = monster
                originalMonsterLore = monsterLore
                setMetadata(monster, monsterLore?.entries.orEmpty())
                updateMonster()
            }
            .launchIn(scope)
    }

    private fun observeEvents() {
        eventManager.events
            .flowOn(dispatcher)
            .onEach { event ->
                when (event) {
                    MonsterRegistrationEvent.Hide -> {
                        analytics.trackMonsterRegistrationClosed(state.value.monster.index)
                        setState { copy(isOpen = false, isSaveButtonEnabled = false) }
                    }

                    is MonsterRegistrationEvent.ShowEdit -> {
                        analytics.trackMonsterRegistrationOpened(event.monsterIndex)
                        params.value = MonsterRegistrationParams(monsterIndex = event.monsterIndex)
                        setState {
                            copy(
                                isOpen = true,
                                strings = appLocalization.getStrings(),
                                isTableContentOpen = false
                            )
                        }
                        fetchMonster()
                    }
                }
            }
            .launchIn(scope)
    }

    private fun setMetadata(monster: Monster, monsterLoreEntries: List<MonsterLoreEntry>) {
        val filteredSavingThrowTypes = AbilityScoreType.entries.filterNot { type ->
            monster.savingThrows.any { it.type == type }
        }
        val filteredDamageVulnerabilityTypes = DamageType.entries.filterNot { type ->
            monster.damageVulnerabilities.any { it.type == type }
        }
        val filteredDamageResistanceTypes = DamageType.entries.filterNot { type ->
            monster.damageResistances.any { it.type == type }
        }
        val filteredDamageImmunityTypes = DamageType.entries.filterNot { type ->
            monster.damageImmunities.any { it.type == type }
        }
        val filteredConditionTypes = ConditionType.entries.filterNot { type ->
            monster.conditionImmunities.any { it.type == type }
        }

        metadata = metadata.copy(
            monster = monster,
            filteredSavingThrowTypes = filteredSavingThrowTypes,
            filteredDamageVulnerabilityTypes = filteredDamageVulnerabilityTypes,
            filteredDamageResistanceTypes = filteredDamageResistanceTypes,
            filteredDamageImmunityTypes = filteredDamageImmunityTypes,
            filteredConditionTypes = filteredConditionTypes,
            monsterLoreEntries = monsterLoreEntries,
        )
    }
}

internal data class Metadata(
    val monster: Monster = Monster(index = ""),
    val filteredSavingThrowTypes: List<AbilityScoreType> = emptyList(),
    val filteredDamageVulnerabilityTypes: List<DamageType> = emptyList(),
    val filteredDamageResistanceTypes: List<DamageType> = emptyList(),
    val filteredDamageImmunityTypes: List<DamageType> = emptyList(),
    val filteredConditionTypes: List<ConditionType> = emptyList(),
    val monsterLoreEntries: List<MonsterLoreEntry> = emptyList(),
)
