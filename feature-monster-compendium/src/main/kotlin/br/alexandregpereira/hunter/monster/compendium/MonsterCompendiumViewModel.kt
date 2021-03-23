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
import br.alexandregpereira.hunter.domain.GetMonstersBySectionUseCase
import br.alexandregpereira.hunter.domain.MonsterPair
import br.alexandregpereira.hunter.domain.MonstersBySection
import br.alexandregpereira.hunter.domain.collections.map
import br.alexandregpereira.hunter.domain.model.Event
import br.alexandregpereira.hunter.domain.model.MonsterSection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

typealias MonsterRow = Map<MonsterCardItem, MonsterCardItem?>
typealias MonsterCardItemsBySection = Map<MonsterSection, MonsterRow>

internal class MonsterCompendiumViewModel(
    private val getMonstersBySectionUseCase: GetMonstersBySectionUseCase
) : ViewModel() {

    private val _stateLiveData = MutableLiveData<MonsterCompendiumViewState>()
    val stateLiveData: LiveData<MonsterCompendiumViewState> = _stateLiveData

    private val _actionLiveData = MutableLiveData<Event<MonsterCompendiumAction>>()
    val actionLiveData: LiveData<Event<MonsterCompendiumAction>> = _actionLiveData

    fun loadMonsters() = viewModelScope.launch {
        getMonstersBySectionUseCase()
            .onStart {
                _stateLiveData.value = MonsterCompendiumViewState(isLoading = true)
            }
            .toMonstersBySection()
            .catch {
                Log.e("MonsterViewModel", it.message ?: "")
            }
            .collect {
                _stateLiveData.value = MonsterCompendiumViewState(
                    monstersBySection = it
                )
            }
    }

    fun navigateToDetail(index: String) {
        _actionLiveData.value = Event(MonsterCompendiumAction.NavigateToDetail(index))
    }

    private fun Flow<MonstersBySection>.toMonstersBySection(): Flow<MonsterCardItemsBySection> {
        return this.map {
            it.map { key, value ->
                key to value.toMonsterRow()
            }
        }
    }

    private fun MonsterPair.toMonsterRow(): MonsterRow {
        return this.map { key, value ->
            key.toMonsterCardItem() to value?.toMonsterCardItem()
        }
    }
}