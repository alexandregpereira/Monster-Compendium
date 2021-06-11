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
import br.alexandregpereira.hunter.domain.model.AbilityScore
import br.alexandregpereira.hunter.domain.model.AbilityScoreType
import br.alexandregpereira.hunter.ui.compose.Window

@Composable
fun AbilityScore(
    abilityScore: AbilityScore,
    modifier: Modifier = Modifier
) = Box(
    modifier = modifier
        .alpha(0.7f),
    contentAlignment = Alignment.Center
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_ability_score),
        contentDescription = abilityScore.type.name,
        modifier = Modifier
            .width(69.dp)
            .height(89.dp)
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.height(89.dp)
    ) {
        Text(
            text = abilityScore.type.name.substring(0..2),
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
fun AbilityScorePreview() = Window {
    AbilityScore(
        abilityScore = AbilityScore(
            type = AbilityScoreType.CHARISMA,
            value = 20,
            modifier = 5
        ),
    )
}