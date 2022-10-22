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

package br.alexandregpereira.hunter.detail.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.detail.R
import br.alexandregpereira.hunter.ui.compose.Window

@Composable
fun AbilityScore(
    abilityScore: AbilityScoreState,
    modifier: Modifier = Modifier
) = Box(
    modifier = modifier
        .alpha(0.7f),
    contentAlignment = Alignment.Center
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_ability_score),
        contentDescription = abilityScore.name,
        modifier = Modifier
            .width(69.dp)
            .height(89.dp)
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.height(89.dp)
    ) {
        Text(
            text = abilityScore.name.substring(0..2),
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = abilityScore.value.toString(),
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
        )

        val abilityScoreModifier = if (abilityScore.modifier > 0) {
            "+${abilityScore.modifier}"
        } else abilityScore.modifier.toString()
        Text(
            text = abilityScoreModifier,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 1.dp)
        )
    }
}

@Preview
@Composable
private fun AbilityScorePreview() = Window {
    AbilityScore(
        abilityScore = AbilityScoreState(
            name ="CHARISMA",
            value = 20,
            modifier = 5
        ),
    )
}