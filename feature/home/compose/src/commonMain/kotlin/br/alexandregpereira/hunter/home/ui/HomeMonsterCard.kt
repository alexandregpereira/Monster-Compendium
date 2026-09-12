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

package br.alexandregpereira.hunter.home.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.ui.compendium.monster.ColorState
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterCardState
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterImageState
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterTypeState
import br.alexandregpereira.hunter.ui.compose.AppImageContentScale
import br.alexandregpereira.hunter.ui.compose.MonsterCard
import br.alexandregpereira.hunter.ui.compose.MonsterCardSize
import br.alexandregpereira.hunter.ui.compose.PreviewWindow

@Composable
internal fun HomeMonsterCard(
    monster: MonsterCardState,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    val image = monster.imageState
    MonsterCard(
        name = monster.name,
        url = image.url,
        icon = image.type.icon,
        backgroundColor = image.backgroundColor.getColor(isSystemInDarkTheme()),
        challengeRating = image.challengeRating,
        contentScale = AppImageContentScale.Crop,
        size = MonsterCardSize.Compact,
        // The height comes from the compact aspect ratio
        modifier = modifier.width(HomeMonsterCardWidth),
        onCLick = onClick,
    )
}

private val HomeMonsterCardWidth = 104.dp

@Preview
@Composable
private fun HomeMonsterCardPreview() = PreviewWindow(darkTheme = true) {
    HomeMonsterCard(
        monster = MonsterCardState(
            index = "aboleth",
            name = "Ancient Red Dragon",
            imageState = MonsterImageState(
                url = "",
                type = MonsterTypeState.DRAGON,
                backgroundColor = ColorState(light = "#E8DCE0", dark = "#E8DCE0"),
                challengeRating = "24",
                contentScale = AppImageContentScale.Fit,
            ),
        ),
        modifier = Modifier.padding(16.dp),
    )
}
