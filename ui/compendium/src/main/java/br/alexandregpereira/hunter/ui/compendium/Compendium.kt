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

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <CardState> Compendium(
    monstersBySection: Map<SectionState, List<RowState<CardState>>>,
    listState: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    key: (RowState<CardState>) -> Any? = { null },
    cardContent: @Composable (CardState) -> Unit,
) = Surface {
    LazyColumn(
        state = listState,
        contentPadding = contentPadding,
    ) {

        monstersBySection.entries.forEach { sectionEntry ->
            val section = sectionEntry.key
            val rows = sectionEntry.value

            val sectionTitlePaddingTop = 32.dp
            val sectionTitlePaddingBottom = 16.dp
            item(key = section.id) {
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

            rows.forEach { row ->
                item(key = key(row)) {
                    val leftCard = row.leftCardState
                    val rightCard = row.rightCardState

                    CompendiumRow(
                        leftCard = leftCard,
                        rightCard = rightCard,
                        modifier = Modifier
                            .padding(vertical = 8.dp, horizontal = 16.dp),
                        content = cardContent,
                    )
                }
            }
        }
    }
}
