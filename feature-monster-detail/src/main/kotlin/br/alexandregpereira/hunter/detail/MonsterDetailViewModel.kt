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

package br.alexandregpereira.hunter.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.alexandregpereira.hunter.domain.GetMonstersByInitialIndexUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

internal class MonsterDetailViewModel(
    private val getMonstersByInitialIndexUseCase: GetMonstersByInitialIndexUseCase
) : ViewModel() {

    private val _stateLiveData = MutableLiveData<MonsterDetailViewState>()
    val stateLiveData: LiveData<MonsterDetailViewState> = _stateLiveData

    fun getMonstersByInitialIndex(index: String) = viewModelScope.launch {
        getMonstersByInitialIndexUseCase(index)
            .onStart {
                _stateLiveData.value = MonsterDetailViewState(isLoading = true)
            }
            .catch {
                Log.e("MonsterDetailViewModel", it.message ?: "")
            }
            .collect {
                _stateLiveData.value = MonsterDetailViewState(
                    initialMonsterIndex = it.first,
                    monsters = it.second
                )
            }
    }
}