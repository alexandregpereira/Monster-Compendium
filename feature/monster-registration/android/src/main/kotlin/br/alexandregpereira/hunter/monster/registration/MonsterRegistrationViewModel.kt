package br.alexandregpereira.hunter.monster.registration

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import br.alexandregpereira.hunter.state.StateHolder

class MonsterRegistrationViewModel internal constructor(
    private val savedStateHandle: SavedStateHandle,
    private val stateHolder: MonsterRegistrationStateHolder,
) : ViewModel(),
    StateHolder<MonsterRegistrationState> by stateHolder,
    MonsterRegistrationIntent by stateHolder {

    override fun onCleared() {
        super.onCleared()
        stateHolder.onCleared()
    }
}