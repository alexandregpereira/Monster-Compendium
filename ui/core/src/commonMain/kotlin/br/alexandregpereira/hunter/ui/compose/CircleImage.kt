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

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CircleImage(
    imageUrl: String,
    backgroundColor: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
) {
    MonsterCoilImage(
        imageUrl = imageUrl,
        contentDescription = contentDescription,
        shape = CircleShape,
        backgroundColor = backgroundColor,
        contentScale = AppImageContentScale.Crop,
        modifier = modifier
            .size(size)
            .animatePressed(
                pressedScale = 0.8f,
                onClick = onClick,
                onLongClick = onLongClick
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.surface,
                shape = CircleShape
            )
    )
}
