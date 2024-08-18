/*
 * Copyright 2022 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
