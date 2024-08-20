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

package br.alexandregpereira.hunter.ui.compendium.monster

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.ui.compendium.Compendium
import br.alexandregpereira.hunter.ui.compendium.CompendiumColumns
import br.alexandregpereira.hunter.ui.compendium.CompendiumItemState
import br.alexandregpereira.hunter.ui.compose.AppImageContentScale
import br.alexandregpereira.hunter.ui.compose.LocalScreenSize
import br.alexandregpereira.hunter.ui.compose.MonsterCard
import br.alexandregpereira.hunter.ui.compose.ScreenSizeType
import br.alexandregpereira.hunter.ui.compose.Window
import br.alexandregpereira.hunter.ui.compose.isHeightExpanded
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MonsterCompendium(
    items: List<CompendiumItemState>,
    modifier: Modifier = Modifier,
    animateItems: Boolean = false,
    enableHorizontalImage: Boolean = false,
    listState: LazyGridState = rememberLazyGridState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onItemCLick: (index: String) -> Unit = {},
    onItemLongCLick: (index: String) -> Unit = {},
) {
    val screenSizes = LocalScreenSize.current
    val currentWidth = screenSizes.widthInDp
    val defaultCardWidth = 148
    val cardWidth = when (screenSizes.type) {
        ScreenSizeType.LandscapeExpanded -> if (screenSizes.isHeightExpanded) {
            180
        } else defaultCardWidth
        ScreenSizeType.Portrait,
        ScreenSizeType.LandscapeCompact -> defaultCardWidth
    }
    Compendium(
        items = items,
        modifier = modifier,
        animateItems = animateItems,
        listState = listState,
        contentPadding = contentPadding,
        columns = CompendiumColumns.Adaptive(cardWidth),
        isHorizontalReading = true,
        key = { it.getMonsterCardState().index },
        isHorizontalCard = {
            enableHorizontalImage && it.getMonsterCardState().imageState.isHorizontal &&
                    currentWidth - 56.dp >= cardWidth.dp * 2
        },
        cardContent = { item ->
            val monsterCardState = item.getMonsterCardState()
            MonsterCard(
                name = monsterCardState.name,
                url = monsterCardState.imageState.url,
                icon = monsterCardState.imageState.type.icon,
                backgroundColor = monsterCardState.imageState.backgroundColor.getColor(
                    isSystemInDarkTheme()
                ),
                isHorizontal = enableHorizontalImage && monsterCardState.imageState.isHorizontal,
                challengeRating = monsterCardState.imageState.challengeRating,
                contentScale = monsterCardState.imageState.contentScale,
                onCLick = { onItemCLick(monsterCardState.index) },
                onLongCLick = { onItemLongCLick(monsterCardState.index) }
            )
        }
    )
}

private fun CompendiumItemState.Item.getMonsterCardState() = this.value as MonsterCardState

@Preview
@Composable
private fun MonsterCompendiumPreview() = Window {
    val imageState = MonsterImageState(
        url = "",
        type = MonsterTypeState.ABERRATION,
        challengeRating = "8",
        backgroundColor = ColorState(
            light = "#ffe0e0",
            dark = "#ffe0e0"
        ),
        contentScale = AppImageContentScale.Fit,
    )
    MonsterCompendium(
        items = mutableListOf<CompendiumItemState>(
            CompendiumItemState.Title(value = "Any"),
        ).also {
            it.addAll(
                (0..10).map { i ->
                    CompendiumItemState.Item(
                        value = MonsterCardState(
                            index = "asdasdasd$i",
                            name = "Monster of monsters $i",
                            imageState = imageState.copy(isHorizontal = i == 2),
                        )
                    )
                }
            )
        }
    )
}
