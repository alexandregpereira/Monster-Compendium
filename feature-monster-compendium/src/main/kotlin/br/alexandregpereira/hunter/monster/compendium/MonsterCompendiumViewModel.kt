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
import br.alexandregpereira.hunter.domain.model.Event
import br.alexandregpereira.hunter.domain.model.asEvent
import br.alexandregpereira.hunter.domain.usecase.GetLastCompendiumScrollItemPositionUseCase
import br.alexandregpereira.hunter.domain.usecase.GetMonsterPreviewsBySectionUseCase
import br.alexandregpereira.hunter.domain.usecase.SaveCompendiumScrollItemPositionUseCase
import br.alexandregpereira.hunter.domain.usecase.SyncMonstersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MonsterCompendiumViewModel @Inject constructor(
    private val syncMonstersUseCase: SyncMonstersUseCase,
    private val getMonsterPreviewsBySectionUseCase: GetMonsterPreviewsBySectionUseCase,
    private val getLastCompendiumScrollItemPositionUseCase: GetLastCompendiumScrollItemPositionUseCase,
    private val saveCompendiumScrollItemPositionUseCase: SaveCompendiumScrollItemPositionUseCase,
    private val dispatcher: CoroutineDispatcher,
    @LoadOnInitFlag loadOnInit: Boolean = true,
) : ViewModel() {

    private val _state = MutableStateFlow(MonsterCompendiumViewState.Initial)
    val state: StateFlow<MonsterCompendiumViewState> = _state

    private val _action = MutableStateFlow<Event<MonsterCompendiumAction>?>(null)
    val action: StateFlow<Event<MonsterCompendiumAction>?> = _action

    init {
        startSync()
        if (loadOnInit) loadMonsters()
    }

    fun loadMonsters() = viewModelScope.launch {
        getMonsterPreviewsBySectionUseCase()
            .zip(
                getLastCompendiumScrollItemPositionUseCase()
            ) { monstersBySection, scrollItemPosition ->
                MonsterCompendiumViewState.Initial.complete(
                    monstersBySection = monstersBySection.asState(),
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

    fun navigateToDetail(index: String) {
        _action.value = MonsterCompendiumAction.NavigateToDetail(index).asEvent()
    }

    fun saveCompendiumScrollItemPosition(position: Int) = viewModelScope.launch {
        saveCompendiumScrollItemPositionUseCase(position).collect()
    }

    private fun startSync() = viewModelScope.launch {
        syncMonstersUseCase()
            .flowOn(dispatcher)
            .catch {
                Log.e("MonsterViewModel", it.message ?: "")
                it.printStackTrace()
            }.collect()
    }
}
