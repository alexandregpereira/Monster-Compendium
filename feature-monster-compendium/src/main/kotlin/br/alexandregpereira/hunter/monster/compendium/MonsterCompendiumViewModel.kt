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
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.alexandregpereira.hunter.domain.collections.map
import br.alexandregpereira.hunter.domain.model.Event
import br.alexandregpereira.hunter.domain.model.MonsterPreview
import br.alexandregpereira.hunter.domain.model.MonsterSection
import br.alexandregpereira.hunter.domain.usecase.GetLastCompendiumScrollItemPositionUseCase
import br.alexandregpereira.hunter.domain.usecase.GetMonsterPreviewsBySectionUseCase
import br.alexandregpereira.hunter.domain.usecase.MonsterPair
import br.alexandregpereira.hunter.domain.usecase.MonstersBySection
import br.alexandregpereira.hunter.domain.usecase.SaveCompendiumScrollItemPositionUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch

typealias MonsterRow = Pair<MonsterPreview, MonsterPreview?>
typealias MonsterCardItemsBySection = Map<MonsterSection, List<MonsterRow>>

internal class MonsterCompendiumViewModel(
    private val getMonsterPreviewsBySectionUseCase: GetMonsterPreviewsBySectionUseCase,
    private val getLastCompendiumScrollItemPositionUseCase: GetLastCompendiumScrollItemPositionUseCase,
    private val saveCompendiumScrollItemPositionUseCase: SaveCompendiumScrollItemPositionUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    loadOnInit: Boolean = true
) : ViewModel() {

    private val _stateLiveData = MutableLiveData(MonsterCompendiumViewState())
    val stateLiveData: LiveData<MonsterCompendiumViewState> = _stateLiveData

    private val _actionLiveData = MutableLiveData<Event<MonsterCompendiumAction>>()
    val actionLiveData: LiveData<Event<MonsterCompendiumAction>> = _actionLiveData

    init {
        if (loadOnInit) loadMonsters()
    }

    fun loadMonsters() = viewModelScope.launch {
        getMonsterPreviewsBySectionUseCase()
            .zip(
                getLastCompendiumScrollItemPositionUseCase()
            ) { monstersBySection, scrollOffset ->
                scrollOffset to monstersBySection
            }
            .map {
                it.first to it.second.toMonstersBySection()
            }
            .flowOn(dispatcher)
            .onStart {
                _stateLiveData.value = MonsterCompendiumViewState(isLoading = true)
            }
            .catch {
                Log.e("MonsterViewModel", it.message ?: "")
                it.printStackTrace()
            }
            .collect {
                _stateLiveData.value = MonsterCompendiumViewState(
                    monstersBySection = it.second,
                    initialScrollItemPosition = it.first
                )
            }
    }

    fun navigateToDetail(index: String) {
        _actionLiveData.value = Event(MonsterCompendiumAction.NavigateToDetail(index))
    }

    fun saveCompendiumScrollItemPosition(position: Int) = viewModelScope.launch {
        saveCompendiumScrollItemPositionUseCase(position).collect()
    }

    private fun MonstersBySection.toMonstersBySection(): MonsterCardItemsBySection {
        return this.map { key, value ->
            key to value.toMonsterRow()
        }
    }

    private fun List<MonsterPair>.toMonsterRow(): List<MonsterRow> {
        return this.map { pair ->
            pair.first to pair.second
        }
    }
}