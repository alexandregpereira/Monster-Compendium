package br.alexandregpereira.hunter.monster.registration

import br.alexandregpereira.hunter.analytics.Analytics
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.monster.spell.model.SchoolOfMagic
import br.alexandregpereira.hunter.domain.spell.GetSpellUseCase
import br.alexandregpereira.hunter.domain.usecase.GetMonsterUseCase
import br.alexandregpereira.hunter.domain.usecase.SaveMonstersUseCase
import br.alexandregpereira.hunter.event.EventManager
import br.alexandregpereira.hunter.monster.registration.domain.NormalizeMonsterUseCase
import br.alexandregpereira.hunter.monster.registration.domain.filterEmpties
import br.alexandregpereira.hunter.monster.registration.event.MonsterRegistrationEvent
import br.alexandregpereira.hunter.monster.registration.event.MonsterRegistrationResult
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
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalCoroutinesApi::class)
class MonsterRegistrationStateHolder internal constructor(
    private val dispatcher: CoroutineDispatcher,
    private val params: StateHolderParams<MonsterRegistrationParams>,
    private val eventManager: EventManager<MonsterRegistrationEvent>,
    private val eventResultManager: EventManager<MonsterRegistrationResult>,
    private val getMonster: GetMonsterUseCase,
    private val saveMonsters: SaveMonstersUseCase,
    private val normalizeMonster: NormalizeMonsterUseCase,
    private val analytics: Analytics,
    private val spellCompendiumEventDispatcher: SpellCompendiumEventResultDispatcher,
    private val spellDetailEventDispatcher: SpellDetailEventDispatcher,
    private val getSpell: GetSpellUseCase,
) : UiModel<MonsterRegistrationState>(MonsterRegistrationState()),
    MutableActionHandler<MonsterRegistrationAction> by MutableActionHandler(),
    MonsterRegistrationIntent {

    private var originalMonster: Monster? = null

    init {
        observeEvents()
        if (state.value.isOpen) {
            fetchMonster()
        }
    }

    override fun onClose() {
        eventManager.dispatchEvent(MonsterRegistrationEvent.Hide)
    }

    override fun onMonsterChanged(monster: Monster) {
        setState {
            copy(
                monster = monster,
                isSaveButtonEnabled = monster.filterEmpties() != originalMonster
            )
        }
    }

    override fun onSaved() {
        analytics.trackMonsterRegistrationSaved(state.value.monster.index)
        normalizeMonster(state.value.monster)
            .flatMapConcat { monster ->
                saveMonsters(monsters = listOf(monster))
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
        val showEvent = Show(
            spellIndex = spellIndex,
            selectedSpellIndexes = state.value.monster.spellcastings
                .flatMap { it.usages }
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

    private fun updateSpells(currentSpellIndex: String, newSpellIndex: String) {
        spellCompendiumEventDispatcher.dispatchEventResult(SpellCompendiumEvent.Hide)
        getSpell(newSpellIndex)
            .flowOn(dispatcher)
            .onEach { newSpell ->
                setState {
                    copy(
                        monster = monster.copy(
                            spellcastings = monster.spellcastings.map { spellcasting ->
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
                    )
                }
            }
            .launchIn(scope)
    }

    private fun openSpellDetail(spellIndex: String) {
        spellDetailEventDispatcher.dispatchEvent(SpellDetailEvent.ShowSpell(spellIndex))
    }

    private fun fetchMonster() {
        val monsterIndex = params.value.monsterIndex?.takeUnless { it.isBlank() } ?: return

        setState { copy(isLoading = true) }
        getMonster(monsterIndex)
            .flowOn(dispatcher)
            .onEach { monster ->
                originalMonster = monster
                setState { copy(monster = monster, isLoading = false) }
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
                        setState { copy(isOpen = true) }
                        fetchMonster()
                    }
                }
            }
            .launchIn(scope)
    }
}
