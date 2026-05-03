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

package br.alexandregpereira.hunter.shareContent

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import java.awt.Desktop
import java.io.File

@Composable
internal actual fun ShareFileTrigger(filePath: String, onComplete: () -> Unit) {
    LaunchedEffect(filePath) {
        try {
            val path = filePath.removePrefix("file://")
            Desktop.getDesktop().open(File(path).parentFile)
        } catch (_: Exception) {
            // Desktop open not supported on this platform
        }
        onComplete()
    }
}
