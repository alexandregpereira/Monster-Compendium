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

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.ui.R
import br.alexandregpereira.hunter.ui.theme.HunterTheme

@Composable
fun MonsterCard(
    name: String,
    url: String,
    @DrawableRes iconRes: Int,
    backgroundColor: String,
    challengeRating: Float,
    modifier: Modifier = Modifier,
    onCLick: () -> Unit = {},
    onLongCLick: () -> Unit = {},
) {
    Column(
        modifier.animatePressed(
            onClick = onCLick,
            onLongClick = onLongCLick
        )
    ) {
        MonsterImage(
            url = url,
            iconRes = iconRes,
            backgroundColor = backgroundColor,
            challengeRating = challengeRating,
        )

        Text(
            text = name,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 4.dp, start = 4.dp, end = 4.dp)
        )
    }
}

@Preview
@Composable
private fun MonsterCardPreview() {
    HunterTheme {
        MonsterCard(
            name = "Monster of the Monsters",
            url = "asdasdas",
            backgroundColor = "#ffe3ee",
            challengeRating = 18f,
            iconRes = R.drawable.ic_aberration
        )
    }
}
