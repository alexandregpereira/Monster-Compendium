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

package br.alexandregpereira.hunter.monster.content.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.monster.content.MonsterContentManagerState
import br.alexandregpereira.hunter.monster.content.MonsterContentState
import br.alexandregpereira.hunter.ui.compose.AppFullScreen
import br.alexandregpereira.hunter.ui.compose.AppTopBar
import br.alexandregpereira.hunter.ui.compose.EmptyScreenMessage
import br.alexandregpereira.hunter.ui.compose.LoadingScreen
import br.alexandregpereira.hunter.ui.compose.LoadingScreenState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun MonsterContentManagerScreen(
    state: MonsterContentManagerState,
    contentPadding: PaddingValues = PaddingValues(),
    onClose: () -> Unit = {},
    onAddClick: (String) -> Unit = {},
    onRemoveClick: (String) -> Unit = {},
    onPreviewClick: (String, String) -> Unit = { _, _ -> },
    onTryAgain: () -> Unit = {}
) = AppFullScreen(
    isOpen = state.isOpen,
    contentPaddingValues = contentPadding,
    showCloseButton = false,
    onClose = onClose
) {
    val isLoading = state.isLoading
    val showGenericError = state.showGenericError
    val monsterContents = state.monsterContents

    val loadingScreenState = remember(
        isLoading,
        showGenericError,
        monsterContents,
    ) {
        when {
            isLoading -> LoadingScreenState.LoadingScreen
            showGenericError -> LoadingScreenState.Error(Unit)
            else -> LoadingScreenState.Success(monsterContents)
        }
    }
    val density = LocalDensity.current
    val listState = rememberLazyStaggeredGridState()
    var topBarHeight by remember { mutableStateOf(0.dp) }
    Box(Modifier.fillMaxSize()) {
        LoadingScreen<ImmutableList<MonsterContentState>, Unit>(
            state = loadingScreenState,
            errorContent = {
                Box(Modifier.padding(top = topBarHeight)) {
                    EmptyScreenMessage(
                        title = strings.noInternetConnection,
                        buttonText = strings.tryAgain,
                        onButtonClick = onTryAgain,
                    )
                }
            }
        ) { monsterContents ->
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Adaptive(300.dp),
                state = listState,
                modifier = Modifier.padding(horizontal = 8.dp),
                contentPadding = PaddingValues(
                    top = topBarHeight + 8.dp,
                    bottom = 24.dp
                ),
                verticalItemSpacing = 16.dp,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(monsterContents, key = { it.acronym }) { monsterContent ->
                    MonsterContentCard(
                        name = monsterContent.name,
                        originalName = monsterContent.originalName,
                        totalMonsters = monsterContent.totalMonsters,
                        totalSpells = monsterContent.totalSpells,
                        summary = monsterContent.summary,
                        coverImageUrl = monsterContent.coverImageUrl,
                        isAdded = monsterContent.isAdded,
                        isDefault = monsterContent.isDefault,
                        strings = state.strings,
                        onAddClick = { onAddClick(monsterContent.acronym) },
                        onRemoveClick = { onRemoveClick(monsterContent.acronym) },
                        onPreviewClick = {
                            onPreviewClick(
                                monsterContent.acronym,
                                monsterContent.name
                            )
                        },
                    )
                }
            }
        }

        AppTopBar(
            title = state.strings.title,
            listState = listState,
            onBackClick = onClose,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .onSizeChanged { size ->
                    topBarHeight = with(density) { size.height.toDp() }
                },
        )
    }
}

@Preview
@Composable
private fun MonsterContentManagerScreenPreview() {
    MonsterContentManagerScreen(
        state = MonsterContentManagerState(
            monsterContents = (0..10).map {
                MonsterContentState(
                    acronym = "ACR",
                    name = "Monster Content",
                    originalName = "Other Name",
                    totalMonsters = 10,
                    totalSpells = 10,
                    summary = "Summary",
                    coverImageUrl = "https://www.google.com.br/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png",
                    isAdded = true
                )
            }.toImmutableList(),
            isOpen = true
        ),
    )
}
