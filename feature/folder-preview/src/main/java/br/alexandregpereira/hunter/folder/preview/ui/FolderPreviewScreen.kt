/*
 * Copyright (C) 2022 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.folder.preview.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import br.alexandregpereira.hunter.folder.preview.FolderPreviewViewState
import br.alexandregpereira.hunter.folder.preview.getState
import br.alexandregpereira.hunter.ui.theme.HunterTheme

@Composable
internal fun FolderPreviewScreen(
    state: FolderPreviewViewState,
    contentPadding: PaddingValues = PaddingValues(),
    onClick: (index: String) -> Unit = {},
    onLongClick: (index: String) -> Unit = {},
) = HunterTheme {
    Box(Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = state.showPreview,
            enter = slideInVertically { fullHeight -> fullHeight * 2 },
            exit = slideOutVertically { fullHeight -> fullHeight * 2 },
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            FolderPreview(
                monsters = state.monsters,
                contentPadding = contentPadding,
                onClick = onClick,
                onLongClick = onLongClick,
            )
        }
    }
}
