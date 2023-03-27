/*
 * Copyright 2022 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
) = Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier.fillMaxSize()
) {
    if (showCircularLoading) {
        CircularProgressIndicator(
            color = MaterialTheme.colors.onBackground,
            modifier = Modifier.size(40.dp)
        )
    }
}
