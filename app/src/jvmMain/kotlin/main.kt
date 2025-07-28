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
import br.alexandregpereira.hunter.app.ui.resources.Res
import br.alexandregpereira.hunter.app.ui.resources.ic_launcher_foreground
import br.alexandregpereira.hunter.ui.compose.BackDispatcher
import br.alexandregpereira.hunter.ui.compose.LocalBackDispatcher
import org.jetbrains.compose.resources.painterResource
import org.koin.core.context.startKoin

fun main() = application {
    startKoin {
        initKoinModules()
    }
    val backDispatcher = JvmBackDispatcher()
    Window(
        onCloseRequest = ::exitApplication,
        title =  "Monster Compendium",
        icon = painterResource(Res.drawable.ic_launcher_foreground),
        state = rememberWindowState(
            size = DpSize(1600.dp, 900.dp)
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
