/*
 * Hunter - DnD 5th edition monster compendium application
 * Copyright (C) 2021 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.monster.compendium

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.alexandregpereira.hunter.domain.usecase.GetLastCompendiumScrollItemPositionUseCase
import br.alexandregpereira.hunter.domain.usecase.GetMonsterPreviewsBySectionUseCase
import br.alexandregpereira.hunter.domain.usecase.SaveCompendiumScrollItemPositionUseCase
import br.alexandregpereira.hunter.domain.usecase.SyncMonstersUseCase
import br.alexandregpereira.hunter.monster.compendium.ui.Loading
import br.alexandregpereira.hunter.monster.compendium.ui.MonsterCompendiumEvents
import br.alexandregpereira.hunter.monster.compendium.ui.MonsterCompendiumViewState
import br.alexandregpereira.hunter.monster.compendium.ui.MonsterRowState
import br.alexandregpereira.hunter.monster.compendium.ui.SectionState
import br.alexandregpereira.hunter.monster.compendium.ui.alphabetIndex
import br.alexandregpereira.hunter.monster.compendium.ui.alphabetOpened
import br.alexandregpereira.hunter.monster.compendium.ui.complete
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MonsterCompendiumViewModel @Inject constructor(
    private val syncMonstersUseCase: SyncMonstersUseCase,
    private val getMonsterPreviewsBySectionUseCase: GetMonsterPreviewsBySectionUseCase,
    private val getLastCompendiumScrollItemPositionUseCase: GetLastCompendiumScrollItemPositionUseCase,
    private val saveCompendiumScrollItemPositionUseCase: SaveCompendiumScrollItemPositionUseCase,
    private val dispatcher: CoroutineDispatcher,
    @LoadOnInitFlag loadOnInit: Boolean = true,
) : ViewModel(), MonsterCompendiumEvents {

    private val _state = MutableStateFlow(MonsterCompendiumViewState.Initial)
    val state: StateFlow<MonsterCompendiumViewState> = _state

    private val _action = MutableSharedFlow<MonsterCompendiumAction>()
    val action: SharedFlow<MonsterCompendiumAction> = _action

    init {
        startSync()
        if (loadOnInit) loadMonsters()
    }

    fun loadMonsters() = viewModelScope.launch {
        getMonsterPreviewsBySectionUseCase()
            .zip(
                getLastCompendiumScrollItemPositionUseCase()
            ) { monstersBySection, scrollItemPosition ->
                val monstersBySectionState = monstersBySection.asState()
                val alphabet = monstersBySectionState.getAlphabet()
                MonsterCompendiumViewState.Initial.complete(
                    monstersBySection = monstersBySectionState,
                    alphabet = alphabet,
                    alphabetIndex = getAlphabetIndex(
                        scrollItemPosition,
                        alphabet,
                        monstersBySectionState
                    ),
                    initialScrollItemPosition = scrollItemPosition
                )
            }
            .onStart {
                emit(MonsterCompendiumViewState.Initial.Loading)
            }
            .flowOn(dispatcher)
            .catch {
                Log.e("MonsterViewModel", it.message ?: "")
                it.printStackTrace()
            }
            .collect { state ->
                _state.value = state
            }
    }

    override fun onItemCLick(index: String) {
        viewModelScope.launch {
            _action.emit(MonsterCompendiumAction.NavigateToDetail(index))
        }
    }

    override fun onFirstVisibleItemChange(position: Int) {
        saveCompendiumScrollItemPosition(position)
    }

    override fun onAlphabetOpened() = changeAlphabetOpenState(opened = true)

    override fun onAlphabetClosed() = changeAlphabetOpenState(opened = false)

    override fun onAlphabetIndexClicked(alphabetIndex: Int) {
        navigateToCompendiumIndex(alphabetIndex)
    }

    private fun navigateToCompendiumIndex(alphabetIndex: Int) {
        _state.value = state.value.alphabetOpened(alphabetOpened = false)
        viewModelScope.launch {
            flowOf(state.value.monstersBySection)
                .map { monstersBySection ->
                    val alphabet = monstersBySection.getAlphabet()
                    monstersBySection.mapToFirstLetters().indexOf(alphabet[alphabetIndex])
                }
                .flowOn(dispatcher)
                .collect { compendiumIndex ->
                    _action.emit(MonsterCompendiumAction
                        .NavigateToCompendiumIndex(compendiumIndex))
                }
        }
    }

    private fun saveCompendiumScrollItemPosition(position: Int) {
        val monstersBySection = state.value.monstersBySection
        val alphabet = state.value.alphabet
        viewModelScope.launch {
            saveCompendiumScrollItemPositionUseCase(position)
                .map {
                    getAlphabetIndex(position, alphabet, monstersBySection)
                }
                .flowOn(dispatcher)
                .collect { alphabetIndex ->
                    _state.value = state.value.alphabetIndex(alphabetIndex)
                }
        }
    }

    private fun changeAlphabetOpenState(opened: Boolean) {
        _state.value = state.value.alphabetOpened(alphabetOpened = opened)
    }

    private fun startSync() = viewModelScope.launch {
        syncMonstersUseCase()
            .flowOn(dispatcher)
            .catch {
                Log.e("MonsterViewModel", it.message ?: "")
                it.printStackTrace()
            }.collect()
    }

    private fun Map<SectionState, List<MonsterRowState>>.mapToFirstLetters(): List<Char> {
        val monsterAlphabet = mutableListOf<Char>()
        forEach { entry ->
            monsterAlphabet.add(entry.key.title.first())
            monsterAlphabet.addAll(
                entry.value.map { entry.key.title.first() }
            )
        }
        return monsterAlphabet
    }

    private fun Map<SectionState, List<MonsterRowState>>.getAlphabet(): List<Char> {
        return mapToFirstLetters().toSortedSet().toList()
    }

    private fun getAlphabetIndex(
        scrollItemPosition: Int,
        alphabet: List<Char>,
        monstersBySection: Map<SectionState, List<MonsterRowState>>
    ): Int {
        if (monstersBySection.isEmpty()) return 0
        val monsterFirstLetters = monstersBySection.mapToFirstLetters()
        return alphabet.indexOf(monsterFirstLetters[scrollItemPosition])
    }
}
