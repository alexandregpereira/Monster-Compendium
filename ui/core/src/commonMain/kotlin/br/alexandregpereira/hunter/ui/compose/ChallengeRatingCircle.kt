/*
 * Copyright 2022 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.alexandregpereira.hunter.ui.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.ui.theme.HunterTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ChallengeRatingCircle(
    challengeRating: String,
    size: Dp,
    modifier: Modifier = Modifier,
    xp: String = "",
    fontSize: TextUnit = 16.sp,
    contentTopPadding: Dp = 0.dp,
    xpFontSize: TextUnit = 10.sp,
) = Box(
    modifier = modifier
        .widthIn(min = size)
        .height(size + contentTopPadding)
) {
    DrawChallengeRatingCircle(
        color = MaterialTheme.colors.surface,
        canvasSize = size,
        contentTopPadding = contentTopPadding
    )
    Column(
        modifier = Modifier.background(
            color = MaterialTheme.colors.surface,
            shape = RoundedCornerShape(bottomEndPercent = 25)
        )
    ) {
        Spacer(modifier = Modifier.height(4.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(vertical = 4.dp)
                .padding(top = contentTopPadding)
        ) {
            Text(
                challengeRating,
                fontWeight = FontWeight.SemiBold,
                fontSize = fontSize,
                color = MaterialTheme.colors.onSurface,
                textAlign = TextAlign.Center,
                maxLines = 1,
                modifier = Modifier
                    .width(size - 16.dp)
            )

            if (xp.isNotBlank()) {
                Text(
                    xp,
                    fontWeight = FontWeight.Normal,
                    fontSize = xpFontSize,
                    color = MaterialTheme.colors.onSurface,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    modifier = Modifier
                        .padding(start = 4.dp, end = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun DrawChallengeRatingCircle(
    color: Color,
    modifier: Modifier = Modifier,
    canvasSize: Dp = 48.dp,
    contentTopPadding: Dp = 0.dp
) = Canvas(
    modifier
        .width(canvasSize)
        .height(canvasSize + contentTopPadding)
) {
    val width = size.width - 8.dp.toPx()
    val height = size.height - 8.dp.toPx()
    val topPadding = contentTopPadding.toPx()
    drawPath(
        path = Path().apply {
            lineTo(0f, 0f)
            lineTo(width, 0f)
            lineTo(width, topPadding)
            cubicTo(
                x1 = width, y1 = height * 0.7f,
                x2 = width * 0.7f, y2 = height,
                x3 = 0f, y3 = height
            )
            close()
        },
        color = color
    )
}

@Preview
@Composable
private fun ChallengeRatingWithXpPreview() {
    HunterTheme {
        ChallengeRatingCircle(
            challengeRating = "10",
            xp = "111k XP",
            size = 62.dp,
            fontSize = 18.sp,
            xpFontSize = 14.sp,
        )
    }
}

@Preview
@Composable
private fun ChallengeRatingWithXpPreviewWithDifferentSize() {
    HunterTheme {
        ChallengeRatingCircle(
            challengeRating = "10",
            xp = "155k XP",
            size = 56.dp,
            fontSize = 16.sp,
            contentTopPadding = 24.dp
        )
    }
}

@Preview
@Composable
private fun ChallengeRatingPreview() {
    HunterTheme {
        ChallengeRatingCircle(challengeRating = "10", size = 48.dp)
    }
}

@Preview
@Composable
private fun ChallengeRatingPreviewWithDifferentSize() {
    HunterTheme {
        ChallengeRatingCircle(
            challengeRating = "10",
            size = 56.dp,
            fontSize = 16.sp,
            contentTopPadding = 24.dp
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
