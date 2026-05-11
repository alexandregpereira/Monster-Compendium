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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import br.alexandregpereira.file.rememberCompendiumFilePickerLauncher
import br.alexandregpereira.hunter.shareContent.state.ShareContentImportStateHolder
import br.alexandregpereira.hunter.shareContent.state.ShareContentImportUiEvent
import br.alexandregpereira.hunter.shareContent.ui.ShareContentImportBottomSheet
import br.alexandregpereira.hunter.state.compose.launchActionEffect
import br.alexandregpereira.hunter.state.compose.rememberStateHolder

@Composable
fun ShareContentImportFeature() {
    val stateHolder = rememberStateHolder<ShareContentImportStateHolder>()

    val launcher = rememberCompendiumFilePickerLauncher(onResult = stateHolder::onFilePicked)

    stateHolder.launchActionEffect { action ->
        when (action) {
            ShareContentImportUiEvent.PickFile -> launcher.launch()
        }
    }

    val state by stateHolder.state.collectAsState()
    ShareContentImportBottomSheet(
        state = state,
        onImport = stateHolder::onImport,
        onFilePickClick = stateHolder::onFilePickClick,
        onClose = stateHolder::onClose,
    )
}
