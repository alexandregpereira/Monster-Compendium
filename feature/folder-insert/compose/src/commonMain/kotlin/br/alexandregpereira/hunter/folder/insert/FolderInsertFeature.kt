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

package br.alexandregpereira.hunter.folder.insert

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import br.alexandregpereira.hunter.folder.insert.ui.FolderInsertScreen
import org.koin.compose.koinInject

@Composable
fun FolderInsertFeature(
    contentPadding: PaddingValues = PaddingValues()
) {
    val stateHolder: FolderInsertStateHolder = koinInject()

    FolderInsertScreen(
        state = stateHolder.state.collectAsState().value,
        contentPadding = contentPadding,
        onFolderNameFieldChange = stateHolder::onFolderNameFieldChange,
        onFolderSelected = stateHolder::onFolderNameFieldChange,
        onLongClick = stateHolder::onRemoveMonster,
        onSave = stateHolder::onSave,
        onClose = stateHolder::onClose,
        onShare = stateHolder::onShare,
    )
}
