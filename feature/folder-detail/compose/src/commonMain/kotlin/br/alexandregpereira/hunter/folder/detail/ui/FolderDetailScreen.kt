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

package br.alexandregpereira.hunter.folder.detail.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.ui.compendium.CompendiumItemState.Item
import br.alexandregpereira.hunter.ui.compendium.CompendiumItemState.Title
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterCardState
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterCompendium
import br.alexandregpereira.hunter.ui.compose.AppFullScreen
import br.alexandregpereira.hunter.ui.compose.plus

@Composable
internal fun FolderDetailScreen(
    isOpen: Boolean,
    folderName: String,
    monsters: List<MonsterCardState>,
    initialFirstVisibleItemIndex: Int = 0,
    initialFirstVisibleItemScrollOffset: Int = 0,
    contentPadding: PaddingValues = PaddingValues(),
    onItemCLick: (index: String) -> Unit = {},
    onItemLongCLick: (index: String) -> Unit = {},
    onClose: () -> Unit = {},
    onScrollChanges: (Int, Int) -> Unit = { _, _ -> },
) = AppFullScreen(
    isOpen = isOpen,
    contentPaddingValues = contentPadding,
    level = 0,
    onClose = onClose
) {
    val items = listOf(
        Title(
            value = folderName,
            isHeader = true
        ),
    ) + monsters.map {
        Item(value = it)
    }
    val listState: LazyGridState = rememberLazyGridState(
        initialFirstVisibleItemIndex = initialFirstVisibleItemIndex,
        initialFirstVisibleItemScrollOffset = initialFirstVisibleItemScrollOffset
    )
    OnScrollChanges(
        getScrollItemScrollValues = {
            listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset
        },
        onScrollChanges = onScrollChanges,
    )
    MonsterCompendium(
        items = items,
        listState = listState,
        animateItems = true,
        contentPadding = contentPadding + PaddingValues(top = 24.dp),
        onItemCLick = onItemCLick,
        onItemLongCLick = onItemLongCLick
    )
}

@Composable
private fun OnScrollChanges(
    getScrollItemScrollValues: () -> Pair<Int, Int>,
    onScrollChanges: (Int, Int) -> Unit
) {
    val (firstVisibleItemIndex, firstVisibleItemScrollOffset) = getScrollItemScrollValues()
    onScrollChanges(firstVisibleItemIndex, firstVisibleItemScrollOffset)
}
