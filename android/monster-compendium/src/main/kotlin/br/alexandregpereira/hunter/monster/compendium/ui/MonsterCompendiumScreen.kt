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

package br.alexandregpereira.hunter.monster.compendium.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.monster.compendium.MonsterCompendiumViewState
import br.alexandregpereira.hunter.ui.compendium.CompendiumItemState
import br.alexandregpereira.hunter.ui.compose.Closeable
import br.alexandregpereira.hunter.ui.compose.EmptyScreenMessage
import br.alexandregpereira.hunter.ui.compose.LoadingScreen
import br.alexandregpereira.hunter.ui.compose.Window

@Composable
internal fun MonsterCompendiumScreen(
    state: MonsterCompendiumViewState,
    initialScrollItemPosition: Int,
    compendiumIndex: Int = -1,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    events: MonsterCompendiumEvents,
) = Window(Modifier.fillMaxSize()) {
    LoadingScreen<MonsterCompendiumErrorState>(
        state = state.loadingState,
        errorContent = { errorState ->
            EmptyScreenMessage(
                title = stringResource(errorState.titleRes),
                buttonText = stringResource(
                    errorState.buttonTextRes
                ),
                onButtonClick = events::onErrorButtonClick
            )
        }
    ) {
        val listState = rememberLazyGridState(
            initialFirstVisibleItemIndex = initialScrollItemPosition
        )
        MonsterCompendiumScreen(
            items = state.items,
            isShowingMonsterFolderPreview = state.isShowingMonsterFolderPreview,
            popupOpened = state.popupOpened,
            alphabet = state.alphabet,
            tableContent = state.tableContent,
            tableContentIndex = state.tableContentIndex,
            tableContentInitialIndex = state.tableContentInitialIndex,
            alphabetSelectedIndex = state.alphabetSelectedIndex,
            tableContentOpened = state.tableContentOpened,
            listState = listState,
            contentPadding = contentPadding,
            events = events
        )

        OnFirstVisibleItemChange(listState, events::onFirstVisibleItemChange)

        if (compendiumIndex >= 0) {
            LaunchedEffect(compendiumIndex) {
                listState.scrollToItem(compendiumIndex)
            }
        }
    }
}

@Composable
private fun MonsterCompendiumScreen(
    items: List<CompendiumItemState>,
    isShowingMonsterFolderPreview: Boolean,
    popupOpened: Boolean,
    alphabet: List<String>,
    alphabetSelectedIndex: Int,
    tableContent: List<TableContentItemState>,
    tableContentIndex: Int,
    tableContentInitialIndex: Int,
    tableContentOpened: Boolean,
    listState: LazyGridState,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    events: MonsterCompendiumEvents,
) {
    Box(Modifier.fillMaxSize()) {
        MonsterCompendium(
            items = items,
            listState = listState,
            contentPadding = contentPadding,
            onItemCLick = events::onItemCLick,
            onItemLongCLick = events::onItemLongCLick,
        )
        Closeable(opened = popupOpened, onClosed = events::onPopupClosed)
        val paddingBottom by animateDpAsState(
            if (isShowingMonsterFolderPreview) 72.dp else 8.dp
        )

        TableContentPopup(
            alphabet = alphabet,
            tableContent = tableContent,
            alphabetSelectedIndex = alphabetSelectedIndex,
            tableContentSelectedIndex = tableContentIndex,
            tableContentInitialIndex = tableContentInitialIndex,
            opened = popupOpened,
            tableContentOpened = tableContentOpened,
            onOpenButtonClicked = events::onPopupOpened,
            onCloseButtonClicked = events::onPopupClosed,
            onTableContentClicked = events::onTableContentIndexClicked,
            onAlphabetIndexClicked = events::onAlphabetIndexClicked,
            onTableContentClosed = events::onTableContentClosed,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    top = contentPadding.calculateTopPadding(),
                    bottom = contentPadding.calculateBottomPadding() + paddingBottom
                )
                .padding(horizontal = 14.dp)
        )
    }
}

@Composable
private fun OnFirstVisibleItemChange(
    listState: LazyGridState,
    saveCompendiumScrollItemPosition: (position: Int) -> Unit
) {
    val firstVisibleItemIndex by remember { derivedStateOf { listState.firstVisibleItemIndex } }
    saveCompendiumScrollItemPosition(firstVisibleItemIndex)
}
