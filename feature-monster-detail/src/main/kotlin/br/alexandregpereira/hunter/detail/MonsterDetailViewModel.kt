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

package br.alexandregpereira.hunter.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.alexandregpereira.hunter.domain.model.MeasurementUnit
import br.alexandregpereira.hunter.domain.usecase.ChangeMonstersMeasurementUnitUseCase
import br.alexandregpereira.hunter.domain.usecase.GetMonsterDetailUseCase
import br.alexandregpereira.hunter.domain.usecase.MonsterDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

internal class MonsterDetailViewModel(
    private var monsterIndex: String,
    private val getMonsterDetailUseCase: GetMonsterDetailUseCase,
    private val changeMonstersMeasurementUnitUseCase: ChangeMonstersMeasurementUnitUseCase
) : ViewModel() {

    private val _stateLiveData = MutableLiveData(MonsterDetailViewState())
    val stateLiveData: LiveData<MonsterDetailViewState> = _stateLiveData

    init {
        getMonstersByInitialIndex()
    }

    private fun getMonstersByInitialIndex() = viewModelScope.launch {
        getMonsterDetailUseCase(monsterIndex).collectDetail()
    }

    fun setMonsterIndex(index: String) {
        monsterIndex = index
    }

    fun onShowOptionsClicked() {
        _stateLiveData.value = stateLiveData.value?.copy(showOptions = true)
    }

    fun onShowOptionsClosed() {
        _stateLiveData.value = stateLiveData.value?.copy(showOptions = false)
    }

    fun onOptionClicked(option: MonsterDetailOption) {
        _stateLiveData.value = stateLiveData.value?.copy(showOptions = false)
        when (option) {
            MonsterDetailOption.CHANGE_TO_FEET -> changeMeasurementUnit(MeasurementUnit.FEET)
            MonsterDetailOption.CHANGE_TO_METERS -> changeMeasurementUnit(MeasurementUnit.METER)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun changeMeasurementUnit(measurementUnit: MeasurementUnit) = viewModelScope.launch {
        changeMonstersMeasurementUnitUseCase(measurementUnit)
            .flatMapLatest { getMonsterDetailUseCase(monsterIndex) }
            .collectDetail()
    }

    private suspend fun Flow<MonsterDetail>.collectDetail() {
        this.flowOn(Dispatchers.IO)
            .onStart {
                _stateLiveData.value = stateLiveData.value?.copy(isLoading = true)
            }
            .catch {
                Log.e("MonsterDetailViewModel", it.message ?: "")
                it.printStackTrace()
            }.collect {
                val measurementUnit = it.third

                _stateLiveData.value = stateLiveData.value?.copy(
                    isLoading = false,
                    initialMonsterIndex = it.first,
                    monsters = it.second,
                    options = when (measurementUnit) {
                        MeasurementUnit.FEET -> listOf(MonsterDetailOption.CHANGE_TO_METERS)
                        MeasurementUnit.METER -> listOf(MonsterDetailOption.CHANGE_TO_FEET)
                    }
                )
            }
    }
}