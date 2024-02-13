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
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import br.alexandregpereira.hunter.folder.preview.FolderPreviewState
import br.alexandregpereira.hunter.ui.theme.HunterTheme

@Composable
internal fun FolderPreviewScreen(
    state: FolderPreviewState,
    contentPadding: PaddingValues = PaddingValues(),
    onClick: (index: String) -> Unit = {},
    onLongClick: (index: String) -> Unit = {},
    onSave: () -> Unit = {},
) = HunterTheme {
    AnimatedVisibility(
        visible = state.showPreview,
        enter = slideInVertically { fullHeight -> fullHeight * 2 },
        exit = slideOutVertically { fullHeight -> fullHeight * 2 },
    ) {
        Box(Modifier.fillMaxSize()) {
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
}
