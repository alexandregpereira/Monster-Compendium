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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.folder.preview.domain.model.MonsterFolderPreview
import br.alexandregpereira.hunter.ui.compose.AppButton
import br.alexandregpereira.hunter.ui.compose.CircleImage
import br.alexandregpereira.hunter.ui.theme.HunterTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FolderPreview(
    monsters: List<MonsterFolderPreview>,
    saveButtonText: String,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    onClick: (index: String) -> Unit = {},
    onLongClick: (index: String) -> Unit = {},
    onSave: () -> Unit = {},
) {
    Box(modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier
                .height(40.dp + contentPadding.calculateBottomPadding())
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            elevation = 0.dp,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            border = BorderStroke(width = 1.dp, color = MaterialTheme.colors.surface),
            backgroundColor = MaterialTheme.colors.surface.copy(alpha = 0.5f)
        ) {}
        Column {
            Row {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(0.7f),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
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
                }

                AppButton(
                    text = saveButtonText,
                    onClick = onSave,
                    modifier = Modifier.padding(end = 16.dp)
                )
            }

            Spacer(
                modifier = Modifier.height(8.dp + contentPadding.calculateBottomPadding())
            )
        }
    }
}

@Preview
@Composable
private fun FolderPreviewPreview() = HunterTheme {
    FolderPreview(
        saveButtonText = "Save",
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
