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

package br.alexandregpereira.hunter.monster.content.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
            modifier = Modifier.padding(horizontal = 16.dp),
            contentPadding = PaddingValues(
                top = 24.dp + contentPadding.calculateTopPadding(),
                bottom = 24.dp + contentPadding.calculateBottomPadding()
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
                        .padding(bottom = 32.dp)
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
                    modifier = Modifier.fillMaxHeight(),
                    onAddClick = { onAddClick(monsterContent.acronym) },
                    onRemoveClick = { onRemoveClick(monsterContent.acronym) },
                    onPreviewClick = {
                        onPreviewClick(
                            monsterContent.acronym,
                            monsterContent.name
                        )
                    },
                )
                Spacer(modifier = Modifier.padding(bottom = 48.dp))
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
