package br.alexandregpereira.hunter.state.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import br.alexandregpereira.hunter.state.StateHolder
import br.alexandregpereira.hunter.state.UiModel
import org.koin.compose.koinInject

@Composable
inline fun <reified T : StateHolder<*>> rememberStateHolder(
    crossinline onCreated: (T) -> Unit = {},
): T {
    val stateHolder = koinInject<T>()

    DisposableEffect(stateHolder) {
        onCreated(stateHolder)
        onDispose {
            (stateHolder as? UiModel<*>)?.onCleared()
        }
    }

    return stateHolder
}
