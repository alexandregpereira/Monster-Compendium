/*
 * Copyright (C) 2022 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
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

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.ui.compose.CircularLoading
import br.alexandregpereira.hunter.ui.compose.Closeable
import br.alexandregpereira.hunter.ui.theme.HunterTheme

@Composable
fun MonsterCompendiumScreen(
    state: MonsterCompendiumViewState,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    events: MonsterCompendiumEvents,
    compendiumIndex: Int = -1,
) = HunterTheme {
    CircularLoading(state.isLoading) {
        val listState = rememberLazyListState(
            initialFirstVisibleItemIndex = state.initialScrollItemPosition
        )
        MonsterCompendiumScreen(state, listState, contentPadding, events)

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
    state: MonsterCompendiumViewState,
    listState: LazyListState,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    events: MonsterCompendiumEvents,
) {
    Box(Modifier.fillMaxSize()) {
        MonsterCompendium(
            monstersBySection = state.monstersBySection,
            listState = listState,
            contentPadding = contentPadding,
            onItemCLick = events::onItemCLick,
            onItemLongCLick = events::onItemLongCLick,
        )
        Closeable(opened = state.alphabetOpened, onClosed = events::onAlphabetClosed) {
            val paddingBottom by animateDpAsState(
                if (state.isShowingMonsterFolderPreview) 72.dp else 0.dp
            )

            AlphabetIndex(
                alphabet = state.alphabet,
                selectedIndex = state.alphabetIndex,
                opened = state.alphabetOpened,
                onOpenButtonClicked = events::onAlphabetOpened,
                onCloseButtonClicked = events::onAlphabetClosed,
                onAlphabetIndexClicked = events::onAlphabetIndexClicked,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = contentPadding.calculateBottomPadding() + paddingBottom)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}

@Composable
private fun OnFirstVisibleItemChange(
    listState: LazyListState,
    saveCompendiumScrollItemPosition: (position: Int) -> Unit
) {
    saveCompendiumScrollItemPosition(listState.firstVisibleItemIndex)
}
