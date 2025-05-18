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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.monster.content.MonsterContentManagerState
import br.alexandregpereira.hunter.monster.content.MonsterContentState
import br.alexandregpereira.hunter.ui.compose.AppFullScreen
import br.alexandregpereira.hunter.ui.compose.LoadingScreen
import br.alexandregpereira.hunter.ui.compose.SectionTitle
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun MonsterContentManagerScreen(
    state: MonsterContentManagerState,
    contentPadding: PaddingValues = PaddingValues(),
    onClose: () -> Unit = {},
    onAddClick: (String) -> Unit = {},
    onRemoveClick: (String) -> Unit = {},
    onPreviewClick: (String, String) -> Unit = { _, _ -> },
) = AppFullScreen(
    isOpen = state.isOpen,
    contentPaddingValues = contentPadding,
    onClose = onClose
) {
    LoadingScreen(isLoading = state.isLoading) {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(300.dp),
            modifier = Modifier.padding(horizontal = 8.dp),
            contentPadding = PaddingValues(
                top = 56.dp,
                bottom = 24.dp
            ),
            verticalItemSpacing = 16.dp,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item(
                key = "title",
                span = StaggeredGridItemSpan.FullLine
            ) {
                SectionTitle(
                    title = state.strings.title,
                    isHeader = true,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                )
            }

            items(state.monsterContents, key = { it.acronym }) { monsterContent ->
                MonsterContentCard(
                    name = monsterContent.name,
                    originalName = monsterContent.originalName,
                    totalMonsters = monsterContent.totalMonsters,
                    summary = monsterContent.summary,
                    coverImageUrl = monsterContent.coverImageUrl,
                    isEnabled = monsterContent.isEnabled,
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
                    summary = "Summary",
                    coverImageUrl = "https://www.google.com.br/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png",
                    isEnabled = true
                )
            },
            isOpen = true
        ),
    )
}
