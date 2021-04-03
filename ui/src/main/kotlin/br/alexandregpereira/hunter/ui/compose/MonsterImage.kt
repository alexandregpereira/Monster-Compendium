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

import android.graphics.Color.parseColor
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.ui.theme.HunterTheme
import br.alexandregpereira.hunter.ui.theme.Shapes
import com.google.accompanist.coil.CoilImage

@Composable
fun MonsterImage(
    imageUrl: String,
    contentDescription: String,
    challengeRating: Float,
    type: MonsterItemType,
    modifier: Modifier = Modifier,
    backgroundColor: String? = null,
    fullOpen: Boolean = false,
) {
    val shape = if (fullOpen) RectangleShape else Shapes.large
    val iconSize = if (fullOpen) 32.dp else 24.dp
    val height = if (fullOpen) 420.dp else 208.dp
    val challengeRatingSize = if (fullOpen) {
        CHALLENGE_RATING_CIRCLE_SIZE_DEFAULT + 8.dp
    } else CHALLENGE_RATING_CIRCLE_SIZE_DEFAULT
    val challengeRatingFontSize = if (fullOpen) 16.sp else 14.sp
    Box(
        modifier
            .clip(shape)
    ) {
        CoilImage(
            data = imageUrl,
            contentDescription = contentDescription,
            fadeIn = true,
            modifier = Modifier
                .height(height)
                .fillMaxWidth()
                .run {
                    if (fullOpen.not()) {
                        background(
                            color = Color(
                                backgroundColor
                                    .runCatching { parseColor(this) }
                                    .getOrNull() ?: 0
                            ),
                            shape = shape
                        )
                    } else this
                }
        )

        ChallengeRatingCircle(
            challengeRating = challengeRating,
            size = challengeRatingSize,
            fontSize = challengeRatingFontSize
        )

        Box(
            contentAlignment = Alignment.TopEnd,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            Icon(
                painter = painterResource(type.iconRes),
                contentDescription = type.name,
                tint = Color.Black,
                modifier = Modifier
                    .size(iconSize)
                    .alpha(0.7f)
            )
        }
    }
}

@Composable
fun ChallengeRatingCircle(
    challengeRating: Float,
    modifier: Modifier = Modifier,
    size: Dp = CHALLENGE_RATING_CIRCLE_SIZE_DEFAULT,
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
fun MonsterImagePreview() = HunterTheme {
    MonsterImage(
        imageUrl = "asdasdas",
        backgroundColor = "#ffe3ee",
        contentDescription = "Anything",
        challengeRating = 18f,
        type = MonsterItemType.ABERRATION
    )
}

@Preview
@Composable
fun ChallengeRatingPreview() {
    HunterTheme {
        ChallengeRatingCircle(10f)
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

private val CHALLENGE_RATING_CIRCLE_SIZE_DEFAULT = 48.dp
