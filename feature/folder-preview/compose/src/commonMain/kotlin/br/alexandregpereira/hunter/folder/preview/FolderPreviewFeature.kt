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

package br.alexandregpereira.hunter.folder.preview

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import br.alexandregpereira.hunter.folder.preview.ui.FolderPreviewScreen
import org.koin.compose.koinInject

@Composable
fun FolderPreviewFeature(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    val stateHolder: FolderPreviewStateHolder = koinInject()
    val state by stateHolder.state.collectAsState()

    FolderPreviewScreen(
        state = state,
        actionHandler = remember(stateHolder) { stateHolder },
        contentPadding = contentPadding,
        onClick = stateHolder::onItemClick,
        onLongClick = stateHolder::onItemLongClick,
        modifier = modifier,
        onSave = stateHolder::onSave,
        onClear = stateHolder::onClear,
    )
}
