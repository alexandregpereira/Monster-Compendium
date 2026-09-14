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

package br.alexandregpereira.hunter.folder.detail.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.ui.compendium.CompendiumItemState.Item
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterCardState
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterCompendium
import br.alexandregpereira.hunter.ui.compose.AppFullScreen
import br.alexandregpereira.hunter.ui.compose.AppTopBar

@Composable
internal fun FolderDetailScreen(
    isOpen: Boolean,
    folderName: String,
    monsters: List<MonsterCardState>,
    initialFirstVisibleItemIndex: Int = 0,
    initialFirstVisibleItemScrollOffset: Int = 0,
    contentPadding: PaddingValues = PaddingValues(),
    isItemSelectionOpen: Boolean = false,
    itemSelectionText: String = "",
    deleteText: String = "",
    addToPreviewText: String = "",
    onItemCLick: (index: String) -> Unit = {},
    onItemLongCLick: (index: String) -> Unit = {},
    onClose: () -> Unit = {},
    onScrollChanges: (Int, Int) -> Unit = { _, _ -> },
    onItemSelectionClose: () -> Unit = {},
    onItemSelectionDeleteClick: () -> Unit = {},
    onItemSelectionAddToPreviewClick: () -> Unit = {},
) = AppFullScreen(
    isOpen = isOpen,
    contentPaddingValues = contentPadding,
    level = 0,
    showCloseButton = false,
    onClose = onClose
) {
    val items = monsters.map {
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
    val density = LocalDensity.current
    var topBarHeight by remember { mutableStateOf(0.dp) }
    Box(Modifier.fillMaxSize()) {
        val extraBottomPadding = if (isItemSelectionOpen) 200.dp else 0.dp
        MonsterCompendium(
            items = items,
            listState = listState,
            animateItems = true,
            contentPadding = PaddingValues(
                top = topBarHeight,
                bottom = contentPadding.calculateBottomPadding() + extraBottomPadding,
            ),
            onItemCLick = onItemCLick,
            onItemLongCLick = onItemLongCLick
        )

        AppTopBar(
            title = folderName,
            listState = listState,
            onBackClick = onClose,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .onSizeChanged { size ->
                    topBarHeight = with(density) { size.height.toDp() }
                }
                .padding(top = contentPadding.calculateTopPadding()),
        )

        ItemSelection(
            itemSelectionText = itemSelectionText,
            deleteText = deleteText,
            addToPreviewText = addToPreviewText,
            contentBottomPadding = contentPadding.calculateBottomPadding(),
            isOpen = isItemSelectionOpen,
            onClose = onItemSelectionClose,
            onDeleteClick = onItemSelectionDeleteClick,
            onAddToPreviewClick = onItemSelectionAddToPreviewClick,
        )
    }
}

@Composable
private fun OnScrollChanges(
    getScrollItemScrollValues: () -> Pair<Int, Int>,
    onScrollChanges: (Int, Int) -> Unit
) {
    val (firstVisibleItemIndex, firstVisibleItemScrollOffset) = getScrollItemScrollValues()
    onScrollChanges(firstVisibleItemIndex, firstVisibleItemScrollOffset)
}
