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

package br.alexandregpereira.hunter.ui.compose

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.ui.resources.Res
import br.alexandregpereira.hunter.ui.resources.ic_aberration
import br.alexandregpereira.hunter.ui.theme.HunterTheme
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MonsterImage(
    url: String,
    icon: DrawableResource,
    backgroundColor: String,
    challengeRating: String,
    contentScale: AppImageContentScale,
    modifier: Modifier = Modifier,
    borderColor: Color = MaterialTheme.colors.surface,
    contentDescription: String = "",
) {
    val iconSize = 20.dp
    val challengeRatingSize = 64.dp
    val challengeRatingFontSize = 18.sp
    Box(modifier) {
        MonsterCoilImage(
            imageUrl = url,
            contentDescription = contentDescription,
            backgroundColor = backgroundColor,
            contentScale = contentScale,
        )

        ChallengeRatingCircle(
            challengeRating = challengeRating,
            size = challengeRatingSize,
            fontSize = challengeRatingFontSize,
            backgroundColor = borderColor,
        )

        MonsterTypeIcon(
            icon = icon,
            iconSize = iconSize,
            modifier = Modifier.align(Alignment.TopEnd),
            size = challengeRatingSize,
            backgroundColor = borderColor,
        )
    }
}

@Composable
fun Modifier.monsterAspectRatio(
    isHorizontal: Boolean = false,
    widthFraction: Float = 1f,
    maxHeight: Dp = LocalScreenSize.current.heightInDp,
): Modifier {
    val aspectRatio by animateFloatAsState(
        targetValue = getMonsterImageAspectRatio(isHorizontal).value
    )
    return fillMaxWidth(widthFraction).heightIn(max = maxHeight)
        .aspectRatio(aspectRatio)
}

fun getMonsterImageAspectRatio(isHorizontal: Boolean): MonsterImageAspectRatio {
    return if (isHorizontal) {
        MonsterImageAspectRatio(width = 179f, height = 152f)
    } else {
        MonsterImageAspectRatio(width = 9f, height = 16f)
    }
}

data class MonsterImageAspectRatio(
    val width: Float,
    val height: Float,
) {
    val value: Float = width / height

    override fun toString(): String = "${width.toInt()}:${height.toInt()}"
}

@Preview
@Composable
fun MonsterImagePreview() = HunterTheme {
    MonsterImage(
        url = "asdasdas",
        backgroundColor = "#ffe3ee",
        contentDescription = "Anything",
        challengeRating = "18",
        icon = Res.drawable.ic_aberration,
        contentScale = AppImageContentScale.Fit,
    )
}

@Preview
@Composable
fun MonsterImageBlackBackgroundPreview() = HunterTheme {
    MonsterImage(
        url = "asdasdas",
        backgroundColor = "#000000",
        contentDescription = "Anything",
        challengeRating = "18",
        icon = Res.drawable.ic_aberration,
        contentScale = AppImageContentScale.Fit,
    )
}
