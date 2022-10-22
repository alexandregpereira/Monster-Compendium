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
internal fun MonsterCompendiumScreen(
    state: MonsterCompendiumViewState,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    events: MonsterCompendiumEvents,
) = HunterTheme {
    CircularLoading(state.isLoading) {
        val listState = rememberLazyListState(
            initialFirstVisibleItemIndex = state.initialScrollItemPosition
        )
        MonsterCompendiumScreen(state, listState, contentPadding, events)

        OnFirstVisibleItemChange(listState, events::onFirstVisibleItemChange)

        if (state.compendiumIndex >= 0) {
            LaunchedEffect(state.compendiumIndex) {
                listState.scrollToItem(state.compendiumIndex)
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
                if (state.isShowingMonsterFolderPreview) 72.dp else 8.dp
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
