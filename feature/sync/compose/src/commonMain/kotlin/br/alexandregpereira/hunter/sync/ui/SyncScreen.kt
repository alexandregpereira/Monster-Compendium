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

package br.alexandregpereira.hunter.sync.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.alexandregpereira.hunter.sync.SyncState
import br.alexandregpereira.hunter.ui.compose.EmptyScreenMessage
import br.alexandregpereira.hunter.ui.compose.LoadingIndicator
import br.alexandregpereira.hunter.ui.compose.Window

@Composable
fun SyncScreen(
    state: SyncState,
    onTryAgain: () -> Unit = {},
) {
    AnimatedVisibility(
        visible = state.isOpen,
        enter = slideInVertically { fullHeight -> fullHeight },
        exit = slideOutVertically { fullHeight -> fullHeight },
    ) {
        Window(Modifier.fillMaxSize()) {
            Crossfade(targetState = state.hasError) { hasError ->
                if (hasError) {
                    EmptyScreenMessage(
                        title = "No Internet Connection",
                        buttonText = "Try Again",
                        onButtonClick = onTryAgain
                    )
                } else {
                    LoadingIndicator()
                }
            }
        }
    }
}
