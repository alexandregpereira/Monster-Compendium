/*
 * Copyright (c) 2021 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.alexandregpereira.hunter.monster.compendium

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.alexandregpereira.hunter.domain.collections.map
import br.alexandregpereira.hunter.domain.model.Event
import br.alexandregpereira.hunter.domain.model.MonsterSection
import br.alexandregpereira.hunter.domain.usecase.GetLastCompendiumScrollItemPositionUseCase
import br.alexandregpereira.hunter.domain.usecase.GetMonstersBySectionUseCase
import br.alexandregpereira.hunter.domain.usecase.MonsterPair
import br.alexandregpereira.hunter.domain.usecase.MonstersBySection
import br.alexandregpereira.hunter.domain.usecase.SaveCompendiumScrollItemPositionUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch

typealias MonsterRow = Map<MonsterCardItem, MonsterCardItem?>
typealias MonsterCardItemsBySection = Map<MonsterSection, MonsterRow>

internal class MonsterCompendiumViewModel(
    private val getMonstersBySectionUseCase: GetMonstersBySectionUseCase,
    private val getLastCompendiumScrollItemPositionUseCase: GetLastCompendiumScrollItemPositionUseCase,
    private val saveCompendiumScrollItemPositionUseCase: SaveCompendiumScrollItemPositionUseCase,
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
        getMonstersBySectionUseCase()
            .zip(
                getLastCompendiumScrollItemPositionUseCase()
            ) { monstersBySection, scrollOffset ->
                scrollOffset to monstersBySection
            }
            .onStart {
                _stateLiveData.value = MonsterCompendiumViewState(isLoading = true)
            }
            .map {
                it.first to it.second.toMonstersBySection()
            }
            .catch {
                Log.e("MonsterViewModel", it.message ?: "")
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

    private fun MonsterPair.toMonsterRow(): MonsterRow {
        return this.map { key, value ->
            key.toMonsterCardItem() to value?.toMonsterCardItem()
        }
    }

    private fun getMonstersBySectionState(
        monstersBySection: MonsterCardItemsBySection
    ): MonsterCompendiumViewState {
        return _stateLiveData.value!!.copy(
            isLoading = false,
            monstersBySection = monstersBySection
        )
    }
}