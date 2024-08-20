/*
 * Copyright (C) 2024 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
