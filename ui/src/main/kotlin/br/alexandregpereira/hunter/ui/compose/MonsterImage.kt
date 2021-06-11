/*
 * Hunter - DnD 5th edition monster compendium application
 * Copyright (C) 2021 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.ui.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.ui.theme.HunterTheme
import br.alexandregpereira.hunter.ui.theme.Shapes

@Composable
fun MonsterImage(
    imageUrl: String,
    contentDescription: String,
    challengeRating: Float,
    type: MonsterItemType,
    modifier: Modifier = Modifier,
    backgroundColor: String? = null
) {
    val shape = Shapes.large
    val iconSize = 24.dp
    val height = 208.dp
    val challengeRatingSize = 48.dp
    val challengeRatingFontSize = 14.sp
    Box(
        modifier
            .clip(shape)
    ) {
        MonsterCoilImage(
            imageUrl = imageUrl,
            contentDescription = contentDescription,
            height = height,
            backgroundColor = backgroundColor,
            shape = shape,
        )

        ChallengeRatingCircle(
            challengeRating = challengeRating,
            size = challengeRatingSize,
            fontSize = challengeRatingFontSize
        )

        MonsterTypeIcon(type = type, iconSize = iconSize)
    }
}

@Preview
@Composable
fun MonsterImagePreview() = HunterTheme {
    MonsterImage(
        imageUrl = "asdasdas",
        backgroundColor = "#ffe3ee",
        contentDescription = "Anything",
        challengeRating = 18f,
        type = MonsterItemType.ABERRATION
    )
}
