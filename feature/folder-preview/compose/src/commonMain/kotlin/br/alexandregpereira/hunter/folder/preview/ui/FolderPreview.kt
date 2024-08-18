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

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.folder.preview.domain.model.MonsterFolderPreview
import br.alexandregpereira.hunter.ui.compose.AppButtonSize
import br.alexandregpereira.hunter.ui.compose.AppCircleButton
import br.alexandregpereira.hunter.ui.compose.CircleImage
import br.alexandregpereira.hunter.ui.theme.HunterTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FolderPreview(
    monsters: List<MonsterFolderPreview>,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(),
    onClick: (index: String) -> Unit = {},
    onLongClick: (index: String) -> Unit = {},
    onSave: () -> Unit = {},
    onClear: () -> Unit = {},
) {
    Column(modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(8.dp))
        Box(modifier = Modifier.fillMaxWidth()) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                state = lazyListState,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(start = 16.dp, end = 80.dp)
            ) {
                items(monsters, key = { it.index }) { state ->
                    CircleImage(
                        imageUrl = state.imageUrl,
                        backgroundColor = if (isSystemInDarkTheme()) {
                            state.backgroundColorDark
                        } else {
                            state.backgroundColorLight
                        },
                        contentDescription = state.name,
                        modifier = Modifier.animateItemPlacement(),
                        onClick = { onClick(state.index) },
                        onLongClick = { onLongClick(state.index) }
                    )
                }
                item(key = "clear") {
                    AppCircleButton(
                        onClick = onClear,
                        isPrimary = false,
                        modifier = Modifier.animateItemPlacement()
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = null
                        )
                    }
                }
            }

            StickerButtonMoreOptions(
                onClick = onSave
            )
        }

        Spacer(modifier = Modifier.height(8.dp + contentPadding.calculateBottomPadding()))
    }
}

@Composable
private fun StickerButtonMoreOptions(
    onClick: () -> Unit,
) = Box(modifier = Modifier.fillMaxWidth()) {
    Box(Modifier.align(Alignment.CenterEnd)) {
        Spacer(
            modifier = Modifier.height(AppButtonSize.MEDIUM.height.dp)
                .width(48.dp)
                .background(MaterialTheme.colors.background)
                .align(Alignment.CenterEnd)
        )
        AppCircleButton(
            onClick = onClick,
            modifier = Modifier.padding(end = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
private fun FolderPreviewPreview() = HunterTheme {
    FolderPreview(
        monsters = listOf(
            MonsterFolderPreview(
                index = "index1",
                name = "",
                imageUrl = "",
                backgroundColorLight = "#e2e2e2",
                backgroundColorDark = "#e2e2e2",
            ),
            MonsterFolderPreview(
                index = "index2",
                name = "",
                imageUrl = "",
                backgroundColorLight = "#e2e2e2",
                backgroundColorDark = "#e2e2e2",
            )
        )
    )
}
