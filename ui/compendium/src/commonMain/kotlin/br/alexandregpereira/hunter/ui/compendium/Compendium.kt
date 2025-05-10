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

package br.alexandregpereira.hunter.ui.compendium

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.ui.compose.AppCard
import br.alexandregpereira.hunter.ui.compose.SectionTitle
import br.alexandregpereira.hunter.ui.compose.imageCardElevation
import br.alexandregpereira.hunter.ui.compose.imageCardShape
import br.alexandregpereira.hunter.ui.compose.monsterAspectRatio

@Composable
fun Compendium(
    items: List<CompendiumItemState>,
    modifier: Modifier = Modifier,
    animateItems: Boolean = false,
    columns: CompendiumColumns = CompendiumColumns.Fixed(count = 2),
    listState: LazyGridState = rememberLazyGridState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    key: (CompendiumItemState.Item) -> Any? = { null },
    isHorizontalCard: (CompendiumItemState.Item) -> Boolean = { false },
    isHorizontalReading: Boolean = false,
    cardContent: @Composable (CompendiumItemState.Item) -> Unit,
) = LazyVerticalGrid(
    columns = columns.toGridCells(),
    state = listState,
    contentPadding = PaddingValues(
        start = 16.dp,
        end = 16.dp,
        top = contentPadding.calculateTopPadding(),
        bottom = contentPadding.calculateBottomPadding() + 64.dp
    ),
    horizontalArrangement = Arrangement.spacedBy(
        space = 16.dp,
        alignment = Alignment.CenterHorizontally
    ),
    verticalArrangement = Arrangement.spacedBy(
        space = 16.dp,
        alignment = Alignment.CenterVertically
    ),
    modifier = modifier,
) {
    items.forEachIndexed { index, item ->
        val isTileCard = index > 0 && isHorizontalReading
        when (item) {
            is CompendiumItemState.Title -> {
                item(
                    key = item.id,
                    span = {
                        val lineSpan = when {
                            isTileCard -> 1
                            else -> maxLineSpan
                        }
                        GridItemSpan(currentLineSpan = lineSpan)
                    }
                ) {
                    val sectionTitlePaddingTop = 32.dp
                    val sectionTitlePaddingBottom = if (isTileCard) 0.dp else 16.dp
                    val paddingTop = when {
                        isTileCard -> 0.dp
                        item.isHeader -> sectionTitlePaddingTop
                        else -> 24.dp
                    }
                    val titleModifier = Modifier.monsterAspectRatio().takeIf { isTileCard }
                        ?: Modifier
                    AppCard(
                        shape = imageCardShape,
                        elevation = imageCardElevation.takeIf { isTileCard } ?: 0.dp,
                        backgroundColor = colors.surface.takeIf { isTileCard }
                            ?: Color.Transparent,
                        modifier = titleModifier.animateItems(this, animateItems)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center.takeIf { isTileCard }
                                ?: Alignment.CenterStart,
                        ) {
                            SectionTitle(
                                title = item.value,
                                isHeader = item.isHeader,
                                textAlign = TextAlign.Center.takeIf { isTileCard },
                                modifier = Modifier
                                    .padding(
                                        top = paddingTop,
                                        bottom = sectionTitlePaddingBottom
                                    )
                                    .padding(horizontal = 16.dp.takeIf { isTileCard } ?: 0.dp)
                            )
                        }
                    }
                }
            }

            is CompendiumItemState.Item -> {
                item(
                    key = key(item),
                    span = {
                        val lineSpan = if (isHorizontalCard(item)) 2 else 1
                        GridItemSpan(lineSpan)
                    }
                ) {
                    Box(
                        modifier = Modifier.animateItems(this, animateItems)
                    ) {
                        cardContent(item)
                    }
                }
            }
        }
    }
}

sealed class CompendiumColumns {
    data class Fixed(val count: Int) : CompendiumColumns()

    data class Adaptive(
        val minSize: Int,
    ) : CompendiumColumns()

    class FixedSize(val size: Dp) : CompendiumColumns()
}

private fun CompendiumColumns.toGridCells(): GridCells = when (this) {
    is CompendiumColumns.Fixed -> GridCells.Fixed(count)
    is CompendiumColumns.Adaptive -> GridCells.Adaptive(minSize.dp)
    is CompendiumColumns.FixedSize -> GridCells.FixedSize(size)
}

private fun Modifier.animateItems(
    scope: LazyGridItemScope,
    animateItems: Boolean
): Modifier = scope.run {
    if (animateItems) this@animateItems.animateItem() else this@animateItems
}
