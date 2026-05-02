package br.alexandregpereira.hunter.ui.color

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first

internal class ColorPickerDispatcher {

    val events: MutableSharedFlow<String> = MutableSharedFlow(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_LATEST,
    )
    val result: MutableSharedFlow<String> = MutableSharedFlow(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_LATEST,
    )

    suspend fun show(color: String): String {
        events.emit(color)
        return result.first()
    }

    fun onColorPicked(color: String) {
        result.tryEmit(color)
    }
}

private var colorPickerDispatcher: ColorPickerDispatcher? = null

@Composable
internal fun rememberColorPickerDispatcher(init: Boolean = false): ColorPickerDispatcher {
    if (init) {
        DisposableEffect(Unit) {
            onDispose {
                colorPickerDispatcher = null
            }
        }
    }
    return remember {
        colorPickerDispatcher ?: ColorPickerDispatcher().also {
            colorPickerDispatcher = it
        }
    }
}
