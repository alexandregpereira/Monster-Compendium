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
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.alexandregpereira.hunter.detail.domain.GetMonsterDetailUseCase
import br.alexandregpereira.hunter.detail.domain.MonsterDetail
import br.alexandregpereira.hunter.domain.model.MeasurementUnit
import br.alexandregpereira.hunter.domain.usecase.ChangeMonstersMeasurementUnitUseCase
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEvent.HideFolderPreview
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEvent.ShowFolderPreview
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEventDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
internal class MonsterDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMonsterDetailUseCase: GetMonsterDetailUseCase,
    private val changeMonstersMeasurementUnitUseCase: ChangeMonstersMeasurementUnitUseCase,
    private val folderPreviewEventDispatcher: FolderPreviewEventDispatcher,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _state = MutableStateFlow(MonsterDetailViewState.Initial)
    val state: StateFlow<MonsterDetailViewState> = _state

    var monsterIndex: String = savedStateHandle["index"] ?: ""
    private val disablePageScroll: Boolean = savedStateHandle["disablePageScroll"] ?: false
    private val folderName: String = savedStateHandle["folderName"] ?: ""

    init {
        folderPreviewEventDispatcher.dispatchEvent(HideFolderPreview)
        getMonstersByInitialIndex()
    }

    override fun onCleared() {
        folderPreviewEventDispatcher.dispatchEvent(ShowFolderPreview)
        super.onCleared()
    }

    private fun getMonstersByInitialIndex() = viewModelScope.launch {
        getMonsterDetail().collectDetail()
    }

    fun onShowOptionsClicked() {
        _state.value = state.value.ShowOptions
    }

    fun onShowOptionsClosed() {
        _state.value = state.value.HideOptions
    }

    fun onOptionClicked(option: MonsterDetailOptionState) {
        _state.value = state.value.HideOptions
        when (option) {
            MonsterDetailOptionState.CHANGE_TO_FEET -> {
                changeMeasurementUnit(MeasurementUnit.FEET)
            }
            MonsterDetailOptionState.CHANGE_TO_METERS -> {
                changeMeasurementUnit(MeasurementUnit.METER)
            }
        }
    }

    private fun getMonsterDetail(): Flow<MonsterDetail> {
        return getMonsterDetailUseCase(monsterIndex, isSingleMonster = disablePageScroll, folderName)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun changeMeasurementUnit(measurementUnit: MeasurementUnit) = viewModelScope.launch {
        changeMonstersMeasurementUnitUseCase(measurementUnit)
            .flatMapLatest { getMonsterDetail() }
            .collectDetail()
    }

    private suspend fun Flow<MonsterDetail>.collectDetail() {
        this.map {
            Log.i("MonsterDetailViewModel", "collectDetail")
            val measurementUnit = it.third
            getState().complete(
                initialMonsterIndex = it.first,
                monsters = it.second.asState(),
                options = when (measurementUnit) {
                    MeasurementUnit.FEET -> listOf(MonsterDetailOptionState.CHANGE_TO_METERS)
                    MeasurementUnit.METER -> listOf(MonsterDetailOptionState.CHANGE_TO_FEET)
                }
            )
        }.onStart {
            Log.i("MonsterDetailViewModel", "onStart")
            emit(getState().Loading)
        }.onCompletion {
            Log.i("MonsterDetailViewModel", "onCompletion")
            emit(getState().NotLoading)
        }.flowOn(dispatcher)
            .catch {
                Log.e("MonsterDetailViewModel", it.message ?: "")
                it.printStackTrace()
            }.collect { state ->
                _state.value = state
            }
    }

    private suspend fun getState(): MonsterDetailViewState = withContext(Dispatchers.Main) {
        state.value
    }
}
