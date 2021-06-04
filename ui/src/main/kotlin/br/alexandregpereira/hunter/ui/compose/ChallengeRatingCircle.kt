/*
 * Copyright (c) 2021 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.alexandregpereira.hunter.ui.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
        color = challengeRating.getChallengeRatingColor(),
        canvasSize = size
    )
    Text(
        challengeRating.getChallengeRatingFormatted(),
        fontWeight = FontWeight.Normal,
        fontSize = fontSize,
        color = Color.White,
        textAlign = TextAlign.Center,
        maxLines = 1,
        modifier = Modifier
            .width(size - 20.dp)
            .padding(bottom = 16.dp)
    )
}

@Composable
fun DrawChallengeRatingCircle(
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

private fun Float.getChallengeRatingColor(): Color {
    return when (this) {
        in 0f..0.99f -> Color(0xFF13BB10)
        in 1f..4f -> Color.Blue
        in 5f..9f -> Color(0xFFA7A700)
        in 10f..14f -> Color(0xFFEE6902)
        in 15f..19f -> Color(0xFFC20000)
        else -> Color.Black
    }
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
fun ChallengeRatingPreview() {
    HunterTheme {
        ChallengeRatingCircle(10f, 48.dp)
    }
}

@Preview
@Composable
fun ChallengeRatingPreviewWithDifferentSize() {
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
fun DrawChallengeRatingCirclePreview() {
    HunterTheme {
        DrawChallengeRatingCircle(Color.Blue)
    }
}
