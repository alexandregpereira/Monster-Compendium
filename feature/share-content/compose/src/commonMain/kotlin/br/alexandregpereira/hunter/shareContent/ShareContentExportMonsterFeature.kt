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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.alexandregpereira.hunter.shareContent.state.ShareContentExportStateHolder
import br.alexandregpereira.hunter.shareContent.state.ShareContentExportUiEvent
import br.alexandregpereira.hunter.shareContent.ui.LocalStrings
import br.alexandregpereira.hunter.state.compose.rememberStateHolder

@Composable
fun ShareContentExportMonsterFeature() {
    val stateHolder = rememberStateHolder<ShareContentExportStateHolder>()
    val state by stateHolder.state.collectAsState()
    var fileUriToShare by remember { mutableStateOf<String?>(null) }
    CompositionLocalProvider(LocalStrings provides state.strings) {
        fileUriToShare?.let { uri ->
            ShareFileTrigger(
                filePath = uri,
                onClosed = {
                    fileUriToShare = null
                    stateHolder.onClose()
                }
            )
        }
    }

    LaunchedEffect(stateHolder.action) {
        stateHolder.action.collect { action ->
            when (action) {
                is ShareContentExportUiEvent.ShareFile -> {
                    fileUriToShare = action.filePath
                }
            }
        }
    }
}
