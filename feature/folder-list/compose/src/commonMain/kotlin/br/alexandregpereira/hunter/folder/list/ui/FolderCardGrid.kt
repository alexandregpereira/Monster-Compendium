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

package br.alexandregpereira.hunter.folder.list.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.folder.list.FolderCardImageState
import br.alexandregpereira.hunter.folder.list.FolderCardState
import br.alexandregpereira.hunter.ui.compose.SectionTitle
import br.alexandregpereira.hunter.ui.compose.Window
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun FolderCardGrid(
    folders: List<FolderCardState>,
    title: String,
    modifier: Modifier = Modifier,
    initialFirstVisibleItemIndex: Int = 0,
    initialFirstVisibleItemScrollOffset: Int = 0,
    contentPadding: PaddingValues = PaddingValues(),
    onCLick: (String) -> Unit = {},
    onLongCLick: (String) -> Unit = {},
    onScrollChanges: (Int, Int) -> Unit = { _, _ -> },
) {
    val lazyListState = rememberLazyGridState(
        initialFirstVisibleItemIndex = initialFirstVisibleItemIndex,
        initialFirstVisibleItemScrollOffset = initialFirstVisibleItemScrollOffset,
    )
    val getFirstVisibleItemValues = {
        lazyListState.firstVisibleItemIndex to lazyListState.firstVisibleItemScrollOffset
    }
    OnScrollChanges(getFirstVisibleItemValues, onScrollChanges)
    LazyVerticalGrid(
        modifier = modifier,
        state = lazyListState,
        columns = GridCells.Adaptive(minSize = 320.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalArrangement = Arrangement.spacedBy(32.dp),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = 16.dp + contentPadding.calculateTopPadding(),
            bottom = 16.dp + contentPadding.calculateBottomPadding()
        )
    ) {
        item(key = "header", span = { GridItemSpan(maxLineSpan) }) {
            SectionTitle(title = title, isHeader = true)
        }

        items(folders, key = { it.folderName.lowercase() }) { folder ->
            val scale by animateFloatAsState(
                targetValue = if (folder.selected) 0.8f else 1f,
                label = "animateSelected",
            )

            FolderCard(
                folderName = folder.folderName,
                image1 = folder.image1,
                image2 = folder.image2,
                image3 = folder.image3,
                modifier = Modifier
                    .animateItemPlacement()
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    },
                onCLick = { onCLick(folder.folderName) },
                onLongCLick = { onLongCLick(folder.folderName) },
            )
        }
    }
}

@Composable
private fun OnScrollChanges(
    getFirstVisibleItemValues: () -> Pair<Int, Int>,
    onScrollChanges: (Int, Int) -> Unit
) {
    val (firstVisibleItemIndex, firstVisibleItemScrollOffset) = getFirstVisibleItemValues()
    onScrollChanges(
        firstVisibleItemIndex,
        firstVisibleItemScrollOffset
    )
}

@Preview
@Composable
private fun FolderCardGridPreview() = Window {
    FolderCardGrid(
        folders = listOf(
            FolderCardState(
                folderName = "Folder 1",
                image1 = FolderCardImageState(
                    backgroundColorLight = "#e2e2e2"
                ),
            ),
            FolderCardState(
                folderName = "Folder 2",
                image1 = FolderCardImageState(
                    backgroundColorLight = "#e2e2e2"
                ),
                image2 = FolderCardImageState(
                    backgroundColorLight = "#a2a2a2"
                ),
            ),
            FolderCardState(
                folderName = "Folder 3",
                image1 = FolderCardImageState(
                    backgroundColorLight = "#e2e2e2"
                ),
                image2 = FolderCardImageState(
                    backgroundColorLight = "#a2a2a2"
                ),
                image3 = FolderCardImageState(
                    backgroundColorLight = "#b2b2b2"
                ),
            ),
            FolderCardState(
                folderName = "Folder 4",
                image1 = FolderCardImageState(
                    isHorizontalImage = true,
                    backgroundColorLight = "#e2e2e2"
                ),
            ),
            FolderCardState(
                folderName = "Folder 5",
                image1 = FolderCardImageState(
                    isHorizontalImage = true,
                    backgroundColorLight = "#e2e2e2"
                ),
                image2 = FolderCardImageState(
                    backgroundColorLight = "#a2a2a2"
                ),
            ),
            FolderCardState(
                folderName = "Folder 6",
                image1 = FolderCardImageState(
                    isHorizontalImage = true,
                    backgroundColorLight = "#e2e2e2"
                ),
                image2 = FolderCardImageState(
                    backgroundColorLight = "#a2a2a2"
                ),
                image3 = FolderCardImageState(
                    backgroundColorLight = "#b2b2b2"
                ),
            )
        ),
        title = "Folders",
    )
}
