package br.alexandregpereira.hunter.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import br.alexandregpereira.hunter.ui.StateRecovery

@Composable
fun<State> StateRecovery(
    key: String,
    stateRecovery: StateRecovery<State>,
    stateSaver: Saver<State, out Any>
) {
    var state by rememberSaveable(
        key = key,
        stateSaver = stateSaver,
    ) {
        mutableStateOf(stateRecovery.state)
    }
    LaunchedEffect(key1 = stateRecovery) {
        stateRecovery.saveState(state)
        stateRecovery.onStateChanges.collect {
            state = it
        }
    }
}
