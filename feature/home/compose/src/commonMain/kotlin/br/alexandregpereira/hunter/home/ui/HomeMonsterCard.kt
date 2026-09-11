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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.ui.compendium.monster.ColorState
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterCardState
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterImageState
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterTypeState
import br.alexandregpereira.hunter.ui.compose.AppImageContentScale
import br.alexandregpereira.hunter.ui.compose.MonsterCoilImage
import br.alexandregpereira.hunter.ui.compose.PreviewWindow
import br.alexandregpereira.hunter.ui.compose.animatePressed

internal val homeCardShape = RoundedCornerShape(12.dp)

@Composable
internal fun HomeMonsterCard(
    monster: MonsterCardState,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) = Column(
    modifier = modifier
        .width(HomeMonsterCardSize)
        .animatePressed(onClick = onClick),
) {
    val image = monster.imageState
    Box(
        modifier = Modifier
            .size(HomeMonsterCardSize)
            .clip(homeCardShape),
    ) {
        MonsterCoilImage(
            imageUrl = image.url,
            contentDescription = image.contentDescription,
            backgroundColor = image.backgroundColor.getColor(isSystemInDarkTheme()),
            contentScale = AppImageContentScale.Crop,
        )
        HomeChallengeRatingBadge(
            challengeRating = image.challengeRating,
            modifier = Modifier.align(Alignment.TopStart),
        )
        HomeMonsterTypeBadge(
            icon = image.type.icon,
            modifier = Modifier.align(Alignment.TopEnd),
        )
    }
    Text(
        text = monster.name,
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.padding(top = 8.dp),
    )
}

private val HomeMonsterCardSize = 104.dp

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
