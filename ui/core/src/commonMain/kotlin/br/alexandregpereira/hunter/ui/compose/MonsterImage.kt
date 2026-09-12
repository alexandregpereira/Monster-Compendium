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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.ui.resources.Res
import br.alexandregpereira.hunter.ui.resources.ic_aberration
import br.alexandregpereira.hunter.ui.theme.HunterTheme
import org.jetbrains.compose.resources.DrawableResource

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
    size: MonsterCardSize = MonsterCardSize.Vertical,
) {
    val iconSize = size.typeIconSize
    val challengeRatingSize = size.badgeSize
    val challengeRatingFontSize = size.challengeRatingFontSize
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
): Modifier = monsterAspectRatio(
    size = isHorizontal.toMonsterCardSize(),
    widthFraction = widthFraction,
    maxHeight = maxHeight,
)

@Composable
fun Modifier.monsterAspectRatio(
    size: MonsterCardSize,
    widthFraction: Float = 1f,
    maxHeight: Dp = LocalScreenSize.current.heightInDp,
): Modifier {
    val aspectRatio by animateFloatAsState(
        targetValue = size.aspectRatio.value
    )
    return fillMaxWidth(widthFraction).heightIn(max = maxHeight)
        .aspectRatio(aspectRatio)
}

fun getMonsterImageAspectRatio(isHorizontal: Boolean): MonsterImageAspectRatio {
    return isHorizontal.toMonsterCardSize().aspectRatio
}

data class MonsterImageAspectRatio(
    val width: Float,
    val height: Float,
) {
    val value: Float = width / height

    override fun toString(): String = "${width.toInt()}:${height.toInt()}"
}

/**
 * The sizes of the monster card. [Compact] is a less vertical card with smaller badges and a single
 * name line, for small cards like the ones in the Home rows.
 */
enum class MonsterCardSize(
    val aspectRatio: MonsterImageAspectRatio,
    val badgeSize: Dp,
    val challengeRatingFontSize: TextUnit,
    val typeIconSize: Dp,
    val nameFontSize: TextUnit,
    val nameMaxLines: Int,
) {
    Vertical(
        aspectRatio = MonsterImageAspectRatio(width = 9f, height = 16f),
        badgeSize = 64.dp,
        challengeRatingFontSize = 18.sp,
        typeIconSize = 20.dp,
        nameFontSize = 18.sp,
        nameMaxLines = Int.MAX_VALUE,
    ),
    Horizontal(
        aspectRatio = MonsterImageAspectRatio(width = 179f, height = 152f),
        badgeSize = 64.dp,
        challengeRatingFontSize = 18.sp,
        typeIconSize = 20.dp,
        nameFontSize = 18.sp,
        nameMaxLines = Int.MAX_VALUE,
    ),
    Compact(
        aspectRatio = MonsterImageAspectRatio(width = 4f, height = 5f),
        badgeSize = 48.dp,
        challengeRatingFontSize = 14.sp,
        typeIconSize = 16.dp,
        nameFontSize = 13.sp,
        nameMaxLines = 1,
    ),
}

fun Boolean.toMonsterCardSize(): MonsterCardSize {
    return if (this) MonsterCardSize.Horizontal else MonsterCardSize.Vertical
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
