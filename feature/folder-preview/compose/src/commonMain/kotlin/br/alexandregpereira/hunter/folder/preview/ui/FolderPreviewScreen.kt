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

package br.alexandregpereira.hunter.folder.preview.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import br.alexandregpereira.hunter.folder.preview.FolderPreviewAction
import br.alexandregpereira.hunter.folder.preview.FolderPreviewState
import br.alexandregpereira.hunter.state.ActionHandler
import kotlinx.coroutines.flow.collectLatest

@Composable
internal fun FolderPreviewScreen(
    state: FolderPreviewState,
    actionHandler: ActionHandler<FolderPreviewAction>,
    contentPadding: PaddingValues = PaddingValues(),
    modifier: Modifier = Modifier,
    onClick: (index: String) -> Unit = {},
    onLongClick: (index: String) -> Unit = {},
    onSave: () -> Unit = {},
    onClear: () -> Unit = {},
) = Box(modifier = modifier.fillMaxWidth().animateContentSize(animationSpec = spring())) {
    AnimatedVisibility(
        visible = state.showPreview,
        enter = fadeIn(animationSpec = spring()),
        exit = fadeOut(animationSpec = spring()),
        modifier = modifier
    ) {
        val lazyListState = rememberLazyListState()

        LaunchedEffect(lazyListState, actionHandler) {
            actionHandler.action.collectLatest { event ->
                when (event) {
                    is FolderPreviewAction.ScrollToStart -> {
                        lazyListState.animateScrollToItem(0)
                    }
                }
            }
        }

        FolderPreview(
            monsters = state.monsters,
            lazyListState = lazyListState,
            contentPadding = contentPadding,
            onClick = onClick,
            onLongClick = onLongClick,
            onSave = onSave,
            onClear = onClear,
        )
    }
}
