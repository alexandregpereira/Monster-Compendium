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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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
    isHorizontalImage: Boolean = false,
) = Box(
    modifier
        .clip(Shapes.large)
) {
    val height = if (fullOpen) {
        if (isHorizontalImage) 360.dp else 420.dp
    } else 208.dp

    CoilImage(
        data = imageUrl,
        contentDescription = contentDescription,
        fadeIn = true,
        modifier = Modifier
            .height(height)
            .fillMaxWidth()
            .background(
                color = Color(
                    backgroundColor
                        .runCatching { parseColor(this) }
                        .getOrNull() ?: 0
                ),
                shape = Shapes.large
            )
    )

    ChallengeRatingCircle(
        challengeRating = challengeRating,
        modifier = Modifier.offset(x = -(53.dp), y = -(53.dp))
    )

    val iconSize = if (fullOpen) 32.dp else 24.dp
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

@Composable
fun ChallengeRatingCircle(
    challengeRating: Float,
    modifier: Modifier = Modifier
) = Box(
    modifier = modifier
        .size(90.dp)
        .clip(CircleShape)
        .background(challengeRating.getChallengeRatingColor())
) {
    Text(
        challengeRating.getChallengeRatingFormatted(),
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = Color.White,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .width(29.dp)
            .offset(x = 52.dp, y = 58.dp)
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
