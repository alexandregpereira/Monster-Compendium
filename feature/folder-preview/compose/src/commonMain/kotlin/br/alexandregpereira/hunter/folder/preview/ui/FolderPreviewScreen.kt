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
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import br.alexandregpereira.hunter.folder.preview.FolderPreviewState

@Composable
internal fun FolderPreviewScreen(
    state: FolderPreviewState,
    contentPadding: PaddingValues = PaddingValues(),
    modifier: Modifier = Modifier,
    onClick: (index: String) -> Unit = {},
    onLongClick: (index: String) -> Unit = {},
    onSave: () -> Unit = {},
) = Box(modifier = modifier.fillMaxWidth().animateContentSize()) {
    AnimatedVisibility(
        visible = state.showPreview,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier
    ) {
        FolderPreview(
            monsters = state.monsters,
            saveButtonText = state.strings.save,
            contentPadding = contentPadding,
            onClick = onClick,
            onLongClick = onLongClick,
            onSave = onSave,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
