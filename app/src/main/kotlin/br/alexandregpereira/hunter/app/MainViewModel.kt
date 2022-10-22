/*
 * Copyright (C) 2022 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.app

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.alexandregpereira.hunter.app.MainViewEvent.BottomNavigationItemClick
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEvent
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@HiltViewModel
class MainViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val monsterDetailEventListener: MonsterDetailEventListener
) : ViewModel() {

    private val _state = MutableStateFlow(savedStateHandle.getState())
    val state: StateFlow<MainViewState> = _state

    init {
        observeMonsterDetailEvents()
    }

    private fun observeMonsterDetailEvents() {
        monsterDetailEventListener.events.onEach { event ->
            when (event) {
                is MonsterDetailEvent.Show -> setState { copy(showBottomBar = false) }
                MonsterDetailEvent.Hide -> setState { copy(showBottomBar = true) }
            }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: MainViewEvent) {
        when (event) {
            is BottomNavigationItemClick -> setState { copy(bottomBarItemSelected = event.item) }
        }
    }

    private fun setState(block: MainViewState.() -> MainViewState) {
        _state.value = state.value.block().saveState(savedStateHandle)
    }
}
