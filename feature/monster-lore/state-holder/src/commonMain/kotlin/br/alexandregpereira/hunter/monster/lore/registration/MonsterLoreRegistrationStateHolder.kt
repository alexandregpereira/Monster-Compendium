package br.alexandregpereira.hunter.monster.lore.registration

import br.alexandregpereira.hunter.domain.monster.lore.GetMonsterLoreUseCase
import br.alexandregpereira.hunter.domain.monster.lore.model.MonsterLore
import br.alexandregpereira.hunter.dynamicFormulary.createDynamicFormKeys
import br.alexandregpereira.hunter.localization.AppLocalization
import br.alexandregpereira.hunter.monster.lore.registration.mapper.asState
import br.alexandregpereira.hunter.state.UiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MonsterLoreRegistrationStateHolder internal constructor(
    private val dispatcher: CoroutineDispatcher,
    appLocalization: AppLocalization,
    private val getMonsterLore: GetMonsterLoreUseCase,
) : UiModel<MonsterLoreRegistrationState>(
    MonsterLoreRegistrationState(strings = appLocalization.getStrings())
) {
    private lateinit var monsterLoreIndex: String
    private var isNewMonsterLore = false

    fun fetchMonsterLore(monsterLoreIndex: String) {
        this.monsterLoreIndex = monsterLoreIndex
        getMonsterLore(monsterLoreIndex)
            .flowOn(dispatcher)
            .onEach { monsterLore ->
                isNewMonsterLore = false
                fillFields(monsterLore)
            }
            .catch {
                isNewMonsterLore = true
                fillFields(
                    MonsterLore(index = monsterLoreIndex, name = "", entries = emptyList())
                )
            }
            .launchIn(scope)
    }

    fun onChanged(entries: List<MonsterLoreEntryState>) {
        setState {
            copy(
                entries = entries,
                keysList = entries.createKeys(),
                isSaveButtonEnabled = true,
            )
        }
    }

    private fun fillFields(monsterLore: MonsterLore) {
        val entries = monsterLore.entries.asState().takeIf { it.isNotEmpty() }
            ?: listOf(MonsterLoreEntryState())

        setState {
            copy(
                entries = entries,
                keysList = entries.createKeys(),
                isSaveButtonEnabled = false,
            )
        }
    }

    private fun List<MonsterLoreEntryState>.createKeys(): List<String> {
        val entries = this
        return buildList {
            add("monster-lore-registration-title")
            entries.forEachIndexed { index, monsterLoreEntryState ->
                listOf(monsterLoreEntryState).createDynamicFormKeys(
                    key = monsterLoreEntryState.key,
                    hasTitle = false,
                    getItemKey = { it.key },
                    addKeys = { add("title"); add("description") },
                ).also {
                    addAll(it)
                }
            }
        }
    }
}
