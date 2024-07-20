import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.input.key.Key.Companion.Escape
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType.Companion.KeyDown
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import br.alexandregpereira.hunter.app.HunterApp
import br.alexandregpereira.hunter.app.di.initKoinModules
import br.alexandregpereira.hunter.ui.compose.BackDispatcher
import br.alexandregpereira.hunter.ui.compose.LocalBackDispatcher
import org.koin.core.context.startKoin

fun main() = application {
    startKoin {
        initKoinModules()
    }
    val backDispatcher = JvmBackDispatcher()
    Window(
        onCloseRequest = ::exitApplication,
        title =  "Monster Compendium",
        state = rememberWindowState(
            size = DpSize(600.dp, 800.dp)
        ),
        onKeyEvent = onKeyEvent@ { keyEvent ->
            val keyPressedHandled = keyEvent.asKeyPressedHandled() ?: return@onKeyEvent false
            when (keyPressedHandled) {
                KeyPressedHandled.Esc -> backDispatcher.onBackPressed()
            }
            return@onKeyEvent true
        }
    ) {
        CompositionLocalProvider(
            LocalBackDispatcher provides backDispatcher
        ) {
            HunterApp()
        }
    }
}

private class JvmBackDispatcher : BackDispatcher {
    val onBackPresses: MutableList<() -> Unit> = mutableListOf()

    override fun onBackPressed(block: () -> Unit) {
        onBackPresses.add(block)
    }

    fun onBackPressed() {
        if (onBackPresses.isNotEmpty()) {
            onBackPresses.last().invoke()
            onBackPresses.removeAt(onBackPresses.lastIndex)
        }
    }
}

private fun KeyEvent.asKeyPressedHandled(): KeyPressedHandled? {
    if (type != KeyDown) return null
    return when (key) {
        Escape -> KeyPressedHandled.Esc
        else -> null
    }
}

private enum class KeyPressedHandled {
    Esc
}
