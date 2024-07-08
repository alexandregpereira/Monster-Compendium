package br.alexandregpereira.hunter.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import br.alexandregpereira.hunter.ui.StateRecovery

@Composable
fun StateRecoveryLaunchedEffect(
    key: String,
    stateRecovery: StateRecovery,
) {
    var state: Map<String, Any?> by rememberSaveable(
        key = key,
        stateSaver = mapSaver(
            save = { stateRecoverySaved ->
                mutableMapOf<String, Any?>().apply {
                    stateRecoverySaved.forEach {
                        this[it.key] = it.value
                    }
                }.also {
                    println("$key: compose state saved: $it")
                }
            },
            restore = { mapRestored ->
                mapRestored.forEach {
                    stateRecovery[it.key] = it.value
                }
                stateRecovery.also {
                    println("$key: compose state restored: $it")
                }
            }
        ),
    ) {
        mutableStateOf(stateRecovery)
    }

    LaunchedEffect(key1 = stateRecovery) {
        stateRecovery.save(state)
        stateRecovery.onStateChanges.collect {
            state = it
        }
    }
}
