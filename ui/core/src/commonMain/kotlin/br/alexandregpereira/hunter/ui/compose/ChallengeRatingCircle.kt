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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    xpFontSize: TextUnit = 16.sp,
    backgroundColor: Color = MaterialTheme.colors.surface,
) = Box(
    modifier = modifier
        .widthIn(min = size)
        .height(size + contentTopPadding)
) {
    CornerCircle(
        color = backgroundColor,
        size = size,
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
    ) {
        Column {
            Text(
                challengeRating,
                fontWeight = FontWeight.SemiBold,
                fontSize = fontSize,
                color = MaterialTheme.colors.onSurface,
                textAlign = TextAlign.Center,
                maxLines = 1,
            )
            if (xp.isNotBlank()) {
                Text(
                    xp,
                    fontWeight = FontWeight.Normal,
                    fontSize = xpFontSize,
                    color = MaterialTheme.colors.onSurface,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                )
            }
        }
    }
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
