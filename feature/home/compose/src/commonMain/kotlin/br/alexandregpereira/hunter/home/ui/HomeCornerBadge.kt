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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.ui.compose.Direction
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

/**
 * Small solid corner badge for the compact Home cards. The `ui:core` `CornerCircle` based badges
 * have a fixed size with large inner padding, which clips the content at this size.
 */
@Composable
private fun HomeCornerBadge(
    direction: Direction,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val radius = 12.dp
    val shape = if (direction == Direction.LEFT) {
        RoundedCornerShape(bottomEnd = radius)
    } else {
        RoundedCornerShape(bottomStart = radius)
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .defaultMinSize(minWidth = 28.dp, minHeight = 26.dp)
            .background(color = MaterialTheme.colors.background, shape = shape)
            .padding(horizontal = 6.dp, vertical = 4.dp),
    ) {
        content()
    }
}

@Composable
internal fun HomeChallengeRatingBadge(
    challengeRating: String,
    modifier: Modifier = Modifier,
) = HomeCornerBadge(direction = Direction.LEFT, modifier = modifier) {
    Text(
        text = challengeRating,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colors.onBackground,
        maxLines = 1,
    )
}

@Composable
internal fun HomeMonsterTypeBadge(
    icon: DrawableResource,
    modifier: Modifier = Modifier,
) = HomeCornerBadge(direction = Direction.RIGHT, modifier = modifier) {
    Icon(
        painter = painterResource(icon),
        contentDescription = null,
        tint = MaterialTheme.colors.onBackground,
        modifier = Modifier.size(14.dp),
    )
}
