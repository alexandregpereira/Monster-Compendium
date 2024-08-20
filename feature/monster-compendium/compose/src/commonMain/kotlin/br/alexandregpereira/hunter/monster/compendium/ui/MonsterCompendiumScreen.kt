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

package br.alexandregpereira.hunter.monster.compendium.ui

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.monster.compendium.asState
import br.alexandregpereira.hunter.monster.compendium.asStateTableContentItem
import br.alexandregpereira.hunter.monster.compendium.domain.MonsterCompendiumError
import br.alexandregpereira.hunter.monster.compendium.domain.model.TableContentItem
import br.alexandregpereira.hunter.monster.compendium.state.MonsterCompendiumAction
import br.alexandregpereira.hunter.monster.compendium.state.MonsterCompendiumIntent
import br.alexandregpereira.hunter.monster.compendium.state.MonsterCompendiumItemState
import br.alexandregpereira.hunter.monster.compendium.state.MonsterCompendiumState
import br.alexandregpereira.hunter.state.ActionHandler
import br.alexandregpereira.hunter.ui.compose.EmptyScreenMessage
import br.alexandregpereira.hunter.ui.compose.LoadingScreen
import br.alexandregpereira.hunter.ui.compose.LoadingScreenState
import br.alexandregpereira.hunter.ui.compose.PopupContainer
import br.alexandregpereira.hunter.ui.compose.tablecontent.TableContentPopup
import kotlinx.coroutines.flow.collectLatest

@Composable
internal fun MonsterCompendiumScreen(
    state: MonsterCompendiumState,
    actionHandler: ActionHandler<MonsterCompendiumAction>,
    initialScrollItemPosition: Int,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    events: MonsterCompendiumIntent,
) {
    LoadingScreen<MonsterCompendiumError>(
        modifier = Modifier.fillMaxSize(),
        state = when {
            state.errorState != null -> LoadingScreenState.Error(state.errorState)
            state.isLoading -> LoadingScreenState.LoadingScreen
            else -> LoadingScreenState.Success
        },
        errorContent = {
            EmptyScreenMessage(
                title = state.strings.noInternetConnection,
                buttonText = state.strings.tryAgain,
                onButtonClick = events::onErrorButtonClick
            )
        }
    ) {
        val listState = rememberLazyGridState(
            initialFirstVisibleItemIndex = initialScrollItemPosition
        )
        MonsterCompendiumScreen(
            items = state.items,
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

        LaunchedEffect(actionHandler.action) {
            actionHandler.action.collectLatest { action ->
                when (action) {
                    is MonsterCompendiumAction.GoToCompendiumIndex -> {
                        if (action.shouldAnimate) {
                            listState.animateScrollToItem(action.index)
                        } else {
                            listState.scrollToItem(action.index)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MonsterCompendiumScreen(
    items: List<MonsterCompendiumItemState>,
    popupOpened: Boolean,
    alphabet: List<String>,
    alphabetSelectedIndex: Int,
    tableContent: List<TableContentItem>,
    tableContentIndex: Int,
    tableContentInitialIndex: Int,
    tableContentOpened: Boolean,
    listState: LazyGridState,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    events: MonsterCompendiumIntent,
) {
    PopupContainer(
        isOpened = popupOpened,
        onPopupClosed = events::onPopupClosed,
        content = {
            MonsterCompendium(
                items = remember(items) { items.asState() },
                listState = listState,
                contentPadding = contentPadding,
                onItemCLick = events::onItemClick,
                onItemLongCLick = events::onItemLongClick,
            )
        },
        popupContent = {
            val paddingBottom = 8.dp
            TableContentPopup(
                alphabet = alphabet,
                tableContent = remember(tableContent) { tableContent.asStateTableContentItem() },
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
                    .padding(
                        top = contentPadding.calculateTopPadding(),
                        bottom = paddingBottom
                    )
            )
        }
    )
}

@Composable
private fun OnFirstVisibleItemChange(
    listState: LazyGridState,
    saveCompendiumScrollItemPosition: (position: Int) -> Unit
) {
    val firstVisibleItemIndex by remember { derivedStateOf { listState.firstVisibleItemIndex } }
    saveCompendiumScrollItemPosition(firstVisibleItemIndex)
}
