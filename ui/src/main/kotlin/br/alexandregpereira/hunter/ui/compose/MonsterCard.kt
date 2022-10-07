/*
 * Hunter - DnD 5th edition monster compendium application
 * Copyright (C) 2022 Alexandre Gomes Pereira
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
