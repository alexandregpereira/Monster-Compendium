package br.alexandregpereira.hunter.app.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import br.alexandregpereira.hunter.app.MainViewEvent
import br.alexandregpereira.hunter.app.MainViewState

@Composable
internal actual fun MainScreen(
    state: MainViewState,
    contentPadding: PaddingValues,
    onEvent: (MainViewEvent) -> Unit
) = AppMainScreen(
    state = state,
    contentPadding = contentPadding,
    onEvent = onEvent
)
