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

import br.alexandregpereira.file.FileEntry
import br.alexandregpereira.hunter.analytics.Analytics
import br.alexandregpereira.hunter.domain.model.AbilityScoreType
import br.alexandregpereira.hunter.domain.model.Action
import br.alexandregpereira.hunter.domain.model.ConditionType
import br.alexandregpereira.hunter.domain.model.DamageType
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.monster.lore.GetMonsterLoreUseCase
import br.alexandregpereira.hunter.domain.monster.lore.model.MonsterLore
import br.alexandregpereira.hunter.domain.monster.lore.model.MonsterLoreEntry
import br.alexandregpereira.hunter.domain.monster.spell.model.SchoolOfMagic
import br.alexandregpereira.hunter.domain.monster.spell.model.SpellUsage
import br.alexandregpereira.hunter.domain.spell.GetSpellUseCase
import br.alexandregpereira.hunter.domain.usecase.GetMonsterUseCase
import br.alexandregpereira.hunter.event.EventManager
import br.alexandregpereira.hunter.event.v2.EventListener
import br.alexandregpereira.hunter.localization.AppLocalization
import br.alexandregpereira.hunter.monster.event.MonsterEvent
import br.alexandregpereira.hunter.monster.event.MonsterEventDispatcher
import br.alexandregpereira.hunter.monster.registration.domain.GenerateNewMonster
import br.alexandregpereira.hunter.monster.registration.domain.MonsterRegistrationFileManager
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
import br.alexandregpereira.hunter.spell.event.SpellResult
import br.alexandregpereira.hunter.spell.event.collectOnChanged
import br.alexandregpereira.hunter.state.MutableActionHandler
import br.alexandregpereira.hunter.state.StateHolderParams
import br.alexandregpereira.hunter.state.UiModel
import br.alexandregpereira.hunter.ui.StateRecovery
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
    private val spellResultListener: EventListener<SpellResult>,
    private val fileManager: MonsterRegistrationFileManager,
    private val generateNewMonster: GenerateNewMonster,
    private val monsterEventDispatcher: MonsterEventDispatcher,
    private val stateRecovery: StateRecovery,
) : UiModel<MonsterRegistrationState>(MonsterRegistrationState()),
    MutableActionHandler<MonsterRegistrationAction> by MutableActionHandler(),
    MonsterRegistrationIntent {

    private var originalMonster: Monster? = null
    private var originalMonsterLore: MonsterLore? = null
    private var metadata: Metadata = Metadata()
    private var spellResultJob: Job? = null

    init {
        observeEvents()
        setState { updateState(stateRecovery) }
        if (state.value.isOpen) {
            params.value = stateRecovery.getParams()
            val recoveredMetadata = stateRecovery.getMetadata()
            if (params.value.monsterIndex != null) {
                fetchMonster()
            } else {
                setMetadata(recoveredMetadata.monster, recoveredMetadata.monsterLoreEntries)
                updateMonster()
            }
        }
    }

    override fun onClose() {
        deleteLastSavedImageIfExistsAndClose()
    }

    override fun onMonsterChanged(monster: MonsterState) {
        val newMonster = metadata.monster?.editBy(monster, appLocalization.getStrings())
        val newMonsterLoreEntries = monster.loreEntries.map { it.asDomain() }
        setMetadata(newMonster, newMonsterLoreEntries)
        updateMonster()
    }

    override fun onMonsterImagePickClick() {
        analytics.track(eventName =  "MonsterRegistration - image pick clicked")
        sendAction(MonsterRegistrationAction.PickImage)
    }

    override fun onMonsterImagePicked(file: FileEntry?) {
        if (file == null) {
            analytics.track(eventName = "MonsterRegistration - image pick cancelled")
            return
        }
        analytics.trackMonsterRegistrationImagePicked(metadata.monster?.index.orEmpty())
        flow {
            val imageName = metadata.monster?.index
                ?: throw IllegalStateException("Monster is null")
            val path = fileManager.saveImage(
                file = file,
                imageName = imageName,
            )
            emit(path)
        }.flowOn(dispatcher)
            .onEach { imagePath ->
                val monster = metadata.monster
                val newMonster = monster?.copy(
                    imageData = monster.imageData.copy(url = imagePath)
                )
                setMetadata(newMonster, metadata.monsterLoreEntries)
                updateMonster()
            }
            .catch { cause: Throwable ->
                analytics.logException(cause)
            }
            .launchIn(scope)
    }

    override fun onSaved() {
        val monsterLoreEntries = metadata.monsterLoreEntries
        val originalMonster = originalMonster
        val originalMonsterLore = originalMonsterLore
        val monster = metadata.monster
        if (monster == null) {
            analytics.logException(
                IllegalStateException("Monster Metadata is null when saving on monster registration")
            )
            return
        }
        normalizeMonster(monster)
            .map { monster ->
                analytics.trackMonsterRegistrationSaved(monster)
                saveMonster(monster, originalMonster, monsterLoreEntries, originalMonsterLore)
                monster
            }
            .flowOn(dispatcher)
            .catch { cause: Throwable ->
                analytics.logException(cause)
            }
            .onEach {
                close()
                val monsterIndex = state.value.monster.index
                eventResultManager.dispatchEvent(
                    MonsterRegistrationResult.OnSaved(
                        monsterIndex = monsterIndex
                    )
                )
                if (isMonsterCreation()) {
                    monsterEventDispatcher.dispatchEvent(
                        event = MonsterEvent.OnVisibilityChanges.Show(index = monsterIndex)
                    )
                }
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
        setState { copy(isTableContentOpen = false) }
    }

    override fun onTableContentOpen() {
        analytics.onTableContentOpened()
        setState { copy(isTableContentOpen = true) }
    }

    private fun updateSpells(currentSpellIndex: String, newSpellIndex: String) {
        @Suppress("UnusedFlow")
        spellCompendiumEventDispatcher.dispatchEventResult(SpellCompendiumEvent.Hide)
        getSpell(newSpellIndex)
            .flowOn(dispatcher)
            .onEach { newSpell ->
                fun List<SpellUsage>.replaceSpell() = map { usage ->
                    usage.copy(
                        spells = usage.spells.map { spell ->
                            if (spell.index == currentSpellIndex) {
                                spell.copy(
                                    index = newSpellIndex,
                                    name = newSpell.name,
                                    level = newSpell.level,
                                    school = SchoolOfMagic.valueOf(newSpell.school.name),
                                )
                            } else spell
                        }
                    )
                }

                fun Action.replaceSpell() = copy(
                    spellsByGroup = spellsByGroup.replaceSpell()
                )

                val newMonster = metadata.monster?.copy(
                    spellcastings = metadata.monster?.spellcastings?.map { spellcasting ->
                        spellcasting.copy(usages = spellcasting.usages.replaceSpell())
                    }.orEmpty(),
                    specialAbilities = metadata.monster?.specialAbilities
                        ?.map { it.replaceSpell() }.orEmpty(),
                    actions = metadata.monster?.actions
                        ?.map { it.replaceSpell() }.orEmpty(),
                    bonusActions = metadata.monster?.bonusActions
                        ?.map { it.replaceSpell() }.orEmpty(),
                    legendaryActions = metadata.monster?.legendaryActions
                        ?.map { it.replaceSpell() }.orEmpty(),
                    reactions = metadata.monster?.reactions
                        ?.map { it.replaceSpell() }.orEmpty(),
                )
                metadata = metadata.copy(monster = newMonster)
                stateRecovery.saveMetadata(metadata)
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
                isSaveButtonEnabled = metadata.monster?.filterEmpties() != originalMonster
                        || metadata.monsterLoreEntries != originalMonsterLore?.entries.orEmpty(),
                isLoading = false,
                tableContent = SectionTitle.entries.filter {
                    if (it == SectionTitle.Source) {
                        monsterState.isSourceVisible
                    } else true
                }.associate { it.name to it.name(strings) },
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
                updateMonster(monster, monsterLore)
            }
            .launchIn(scope)
    }

    private fun updateMonster(monster: Monster, monsterLore: MonsterLore?) {
        originalMonster = monster
        originalMonsterLore = monsterLore
        setMetadata(monster, monsterLore?.entries.orEmpty())
        updateMonster()
    }

    private fun observeEvents() {
        eventManager.events
            .flowOn(dispatcher)
            .onEach { event ->
                when (event) {
                    MonsterRegistrationEvent.Hide -> {
                        analytics.trackMonsterRegistrationClosed(state.value.monster.index)
                        setState { copy(isOpen = false, isSaveButtonEnabled = false).saveState(stateRecovery) }
                        fileManager.clear()
                        spellResultJob?.cancel()
                        onCleared()
                    }

                    is MonsterRegistrationEvent.Show -> {
                        analytics.trackMonsterRegistrationOpened(event.monsterIndex)
                        params.value = MonsterRegistrationParams(monsterIndex = event.monsterIndex)
                        stateRecovery.saveParams(params.value)
                        observeSpellResultEvents()
                        setState {
                            copy(
                                isOpen = true,
                                strings = appLocalization.getStrings(),
                                isTableContentOpen = false
                            ).saveState(stateRecovery)
                        }
                        if (event.monsterIndex != null) {
                            fetchMonster()
                        } else {
                            generateNewMonsterState()
                        }
                    }
                }
            }
            .launchIn(scope)
    }

    private fun generateNewMonsterState() {
        val monster = generateNewMonster()
        updateMonster(monster, monsterLore = null)
    }

    private fun deleteLastSavedImageIfExistsAndClose() {
        scope.launch {
            withContext(dispatcher) { fileManager.deleteLastSavedImageIfExists() }
            close()
        }
    }

    private fun observeSpellResultEvents() {
        spellResultJob?.cancel()
        spellResultJob = spellResultListener.collectOnChanged {
            fetchMonster()
        }.launchIn(featureScope)
    }

    private fun setMetadata(monster: Monster?, monsterLoreEntries: List<MonsterLoreEntry>) {
        val filteredSavingThrowTypes = AbilityScoreType.entries.filterNot { type ->
            monster?.savingThrows?.any { it.type == type } ?: false
        }
        val filteredDamageVulnerabilityTypes = DamageType.entries.filterNot { type ->
            monster?.damageVulnerabilities?.any { it.type == type } ?: false
        }
        val filteredDamageResistanceTypes = DamageType.entries.filterNot { type ->
            monster?.damageResistances?.any { it.type == type } ?: false
        }
        val filteredDamageImmunityTypes = DamageType.entries.filterNot { type ->
            monster?.damageImmunities?.any { it.type == type } ?: false
        }
        val filteredConditionTypes = ConditionType.entries.filterNot { type ->
            monster?.conditionImmunities?.any { it.type == type } ?: false
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
        stateRecovery.saveMetadata(metadata)
    }

    private fun close() {
        eventManager.dispatchEvent(MonsterRegistrationEvent.Hide)
    }

    private fun isMonsterCreation(): Boolean = params.value.monsterIndex == null
}

internal data class Metadata(
    val monster: Monster? = null,
    val filteredSavingThrowTypes: List<AbilityScoreType> = emptyList(),
    val filteredDamageVulnerabilityTypes: List<DamageType> = emptyList(),
    val filteredDamageResistanceTypes: List<DamageType> = emptyList(),
    val filteredDamageImmunityTypes: List<DamageType> = emptyList(),
    val filteredConditionTypes: List<ConditionType> = emptyList(),
    val monsterLoreEntries: List<MonsterLoreEntry> = emptyList(),
)
