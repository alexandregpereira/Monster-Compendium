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

package br.alexandregpereira.hunter.ui.compendium

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <CardState> Compendium(
    monstersBySection: Map<SectionState, List<CardState>>,
    listState: LazyGridState = rememberLazyGridState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    key: (CardState) -> Any? = { null },
    isHorizontalCard: (CardState) -> Boolean = { false },
    cardContent: @Composable (CardState) -> Unit,
) = Surface {
    LazyVerticalGrid(
        columns = GridCells.Fixed(count = 2),
        state = listState,
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = contentPadding.calculateTopPadding(),
            bottom = contentPadding.calculateBottomPadding()
        ),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        monstersBySection.entries.forEach { sectionEntry ->
            val section = sectionEntry.key
            val cardStates = sectionEntry.value

            val sectionTitlePaddingTop = 32.dp
            val sectionTitlePaddingBottom = 16.dp
            item(
                key = section.id,
                span = { GridItemSpan(maxLineSpan) }
            ) {
                section.parentTitle?.let {
                    SectionTitle(
                        title = it,
                        isHeader = true,
                        modifier = Modifier.padding(
                            top = sectionTitlePaddingTop,
                            bottom = sectionTitlePaddingBottom
                        )
                    )
                }
                val paddingTop = when {
                    section.parentTitle != null -> 0.dp
                    section.isHeader -> sectionTitlePaddingTop
                    else -> 24.dp
                }
                SectionTitle(
                    title = section.title,
                    isHeader = section.isHeader && section.parentTitle == null,
                    modifier = Modifier.padding(
                        top = paddingTop,
                        bottom = sectionTitlePaddingBottom
                    )
                )
            }

            cardStates.forEach { cardState ->
                item(
                    key = key(cardState),
                    span = {
                        val lineSpan = if (isHorizontalCard(cardState)) {
                            maxLineSpan
                        } else 1
                        GridItemSpan(lineSpan)
                    }
                ) {
                    Box(
                        modifier = Modifier
                            .padding(vertical = 8.dp),
                    ) {
                        cardContent(cardState)
                    }
                }
            }
        }
    }
}
