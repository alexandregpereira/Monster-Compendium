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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf

val LocalBackDispatcher = compositionLocalOf {
    BackDispatcher { println("LocalBackDispatcher not provided") }
}

@Composable
actual fun BackPlatformHandler(enabled: Boolean, onBack: () -> Unit) {
    if (!enabled) return
    LocalBackDispatcher.current.onBackPressed(onBack)
}

fun interface BackDispatcher {
    fun onBackPressed(block: () -> Unit)
}
