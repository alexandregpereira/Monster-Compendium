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

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.ui.compose.LoadingScreenState.Error
import br.alexandregpereira.hunter.ui.compose.LoadingScreenState.LoadingScreen
import br.alexandregpereira.hunter.ui.compose.LoadingScreenState.Success

@Composable
fun LoadingScreen(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    showCircularLoading: Boolean = true,
    content: @Composable () -> Unit
) {
    LoadingScreen<Unit>(
        state = if (isLoading) LoadingScreen else Success,
        modifier = modifier,
        showCircularLoading = showCircularLoading,
        content = content
    )
}

@Composable
inline fun <reified ErrorState> LoadingScreen(
    state: LoadingScreenState,
    modifier: Modifier = Modifier,
    showCircularLoading: Boolean = true,
    crossinline errorContent: @Composable (ErrorState) -> Unit = {},
    crossinline content: @Composable () -> Unit
) {
    Crossfade(targetState = state, modifier = modifier) {
        when (it) {
            LoadingScreen -> LoadingIndicator(showCircularLoading)
            Success -> content()
            is Error<*> -> if (it.errorState is ErrorState) errorContent(it.errorState)
        }
    }
}

sealed class LoadingScreenState {
    object LoadingScreen : LoadingScreenState()
    object Success : LoadingScreenState()
    data class Error<ErrorState>(val errorState: ErrorState) : LoadingScreenState()
}

@Composable
fun LoadingIndicator(
    showCircularLoading: Boolean = true,
    size: Dp = 40.dp
) = Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier.fillMaxSize()
) {
    if (showCircularLoading) {
        CircularProgressIndicator(
            color = MaterialTheme.colors.onBackground,
            modifier = Modifier.size(size)
        )
    }
}
