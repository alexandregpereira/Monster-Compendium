/*
 * Copyright (C) 2026 Alexandre Gomes Pereira
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
import androidx.compose.runtime.DisposableEffect
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

enum class LifecycleEvent {
    ON_PAUSE,
    ON_RESUME
}

/**
 * Observes lifecycle events in a multiplatform way
 */
@Composable
internal expect fun rememberLifecycleEvents(): Flow<LifecycleEvent>

/**
 * A composable that observes lifecycle events and calls the appropriate callbacks
 */
@Composable
fun LifecycleEventObserver(
    onStart: () -> Unit = {},
    onStop: () -> Unit = {},
    onPause: () -> Unit = {},
    onResume: () -> Unit = {}
) {
    val lifecycleEvents = rememberLifecycleEvents()

    DisposableEffect(lifecycleEvents) {
        onStart()
        val job = MainScope().launch {
            lifecycleEvents.collect { event ->
                when (event) {
                    LifecycleEvent.ON_PAUSE -> onPause()
                    LifecycleEvent.ON_RESUME -> onResume()
                }
            }
        }

        onDispose {
            onStop()
            job.cancel()
        }
    }
}
