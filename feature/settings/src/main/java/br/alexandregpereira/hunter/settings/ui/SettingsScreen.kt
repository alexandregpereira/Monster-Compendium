/*
 * Hunter - DnD 5th edition monster compendium application
 * Copyright (C) 2022. Alexandre Gomes Pereira.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.settings.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.alexandregpereira.hunter.settings.SettingsViewState
import br.alexandregpereira.hunter.ui.compose.Window

@Composable
internal fun SettingsScreen(
    state: SettingsViewState,
    contentPadding: PaddingValues,
    onImageBaseUrlChange: (String) -> Unit = {},
    onAlternativeSourceBaseUrlChange: (String) -> Unit = {},
    onSaveButtonClick: () -> Unit = {}
) = Window(modifier = Modifier.fillMaxSize()) {
    Settings(
        imageBaseUrl = state.imageBaseUrl,
        alternativeSourceBaseUrl = state.alternativeSourceBaseUrl,
        saveButtonEnabled = state.saveButtonEnabled,
        onImageBaseUrlChange = onImageBaseUrlChange,
        onAlternativeSourceBaseUrlChange = onAlternativeSourceBaseUrlChange,
        onSaveButtonClick = onSaveButtonClick,
        modifier = Modifier.padding(
            top = contentPadding.calculateTopPadding(),
            bottom = contentPadding.calculateBottomPadding()
        )
    )
}
