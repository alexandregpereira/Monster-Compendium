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

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.ui.theme.HunterTheme

@Composable
fun ChallengeRatingCircle(
    challengeRating: Float,
    size: Dp,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 14.sp
) = Box(
    contentAlignment = Alignment.CenterStart,
    modifier = modifier.size(size)
) {
    DrawChallengeRatingCircle(
        color = MaterialTheme.colors.surface,
        canvasSize = size
    )
    Text(
        challengeRating.getChallengeRatingFormatted(),
        fontWeight = FontWeight.SemiBold,
        fontSize = fontSize,
        color = MaterialTheme.colors.onSurface,
        textAlign = TextAlign.Center,
        maxLines = 1,
        modifier = Modifier
            .width(size - 20.dp)
            .padding(bottom = 16.dp)
    )
}

@Composable
private fun DrawChallengeRatingCircle(
    color: Color,
    modifier: Modifier = Modifier,
    canvasSize: Dp = 48.dp
) = Canvas(modifier.size(canvasSize)) {
    val width = size.width - 8.dp.toPx()
    val height = size.height - 8.dp.toPx()
    drawPath(
        path = Path().apply {
            lineTo(0f, 0f)
            lineTo(width, 0f)
            cubicTo(
                x1 = width, y1 = height / 2,
                x2 = width / 2, y2 = height,
                x3 = 0f, y3 = height
            )
            close()
        },
        color = color
    )
}

private fun Float.getChallengeRatingFormatted(): String {
    return if (this < 1) {
        val value = 1 / this
        "1/${value.toInt()}"
    } else {
        this.toInt().toString()
    }
}



@Preview
@Composable
private fun ChallengeRatingPreview() {
    HunterTheme {
        ChallengeRatingCircle(10f, 48.dp)
    }
}

@Preview
@Composable
private fun ChallengeRatingPreviewWithDifferentSize() {
    HunterTheme {
        ChallengeRatingCircle(
            10f,
            size = 56.dp,
            fontSize = 16.sp
        )
    }
}

@Preview
@Composable
private fun DrawChallengeRatingCirclePreview() {
    HunterTheme {
        DrawChallengeRatingCircle(Color.Blue)
    }
}
