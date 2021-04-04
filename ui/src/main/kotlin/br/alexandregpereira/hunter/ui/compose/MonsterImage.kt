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

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.ui.theme.HunterTheme
import br.alexandregpereira.hunter.ui.theme.Shapes

@Composable
fun MonsterImage(
    imageUrl: String,
    contentDescription: String,
    challengeRating: Float,
    type: MonsterItemType,
    modifier: Modifier = Modifier,
    backgroundColor: String? = null
) {
    val shape = Shapes.large
    val iconSize = 24.dp
    val height = 208.dp
    val challengeRatingSize = 48.dp
    val challengeRatingFontSize = 14.sp
    Box(
        modifier
            .clip(shape)
    ) {
        MonsterCoilImage(
            imageUrl = imageUrl,
            contentDescription = contentDescription,
            height = height,
            backgroundColor = backgroundColor,
            shape = shape,
        )

        ChallengeRatingCircle(
            challengeRating = challengeRating,
            size = challengeRatingSize,
            fontSize = challengeRatingFontSize
        )

        MonsterTypeIcon(type = type, iconSize = iconSize)
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
