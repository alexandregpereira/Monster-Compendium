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
import br.alexandregpereira.hunter.shareContent.ui.exportStrings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.awt.EventQueue
import java.io.File
import javax.swing.JFileChooser

@Composable
internal actual fun ShareFileTrigger(
    filePath: String,
    onClosed: () -> Unit,
) {
    val chooseDestinationFolder = exportStrings.chooseDestinationFolder
    LaunchedEffect(filePath) {
        val sourceFile = File(filePath.removePrefix("file://"))
        withContext(Dispatchers.IO) {
            EventQueue.invokeAndWait {
                try {
                    val chooser = JFileChooser().apply {
                        fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
                        dialogTitle = chooseDestinationFolder
                        currentDirectory = File(System.getProperty("user.home"))
                    }
                    val result = chooser.showSaveDialog(null)
                    if (result == JFileChooser.APPROVE_OPTION) {
                        sourceFile.copyTo(
                            File(chooser.selectedFile, sourceFile.name),
                            overwrite = true
                        )
                    }
                    onClosed()
                } catch (cause: Exception) {
                    cause.printStackTrace()
                    onClosed()
                }
            }
        }
    }
}
