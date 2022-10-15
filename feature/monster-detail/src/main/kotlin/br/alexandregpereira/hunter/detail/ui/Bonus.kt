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

package br.alexandregpereira.hunter.detail.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Bonus(
    value: Int,
    name: String,
    modifier: Modifier = Modifier,
    iconSize: Dp = 56.dp,
    alpha: Float = 0.7f
) {
    Column(
        modifier
            .alpha(alpha)
            .width(80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(contentAlignment = Alignment.Center) {
            BonusImage(size = iconSize)
            Text(
                text = "+$value",
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp
            )
        }

        Text(
            text = name,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            modifier = Modifier.padding(4.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun BonusImage(
    size: Dp = 56.dp,
    color: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
) {
    Canvas(
        Modifier
            .size(size)
            .padding(1.dp)) {
        drawCircle(
            color,
            style = Stroke(width = 2.dp.toPx()),
        )

        drawCircle(
            color,
            radius = (size.toPx() / 2f) - 5.dp.toPx(),
            style = Stroke(width = 1.dp.toPx()),
        )
    }
}

@Preview
@Composable
fun BonusPreview() {
    Bonus(value = 10, name = "Des")
}

@Preview
@Composable
fun BonusImagePreview() {
    BonusImage()
}