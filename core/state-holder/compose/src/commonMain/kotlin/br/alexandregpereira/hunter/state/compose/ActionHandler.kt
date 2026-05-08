package br.alexandregpereira.hunter.state.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import br.alexandregpereira.hunter.state.ActionHandler

@Composable
fun <Action : Any> ActionHandler<Action>.launchActionEffect(
    onAction: suspend (Action) -> Unit,
) {
    LaunchedEffect(this) {
        action.collect { action ->
            onAction(action)
        }
    }
}
