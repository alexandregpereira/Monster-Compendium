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

package br.alexandregpereira.hunter.monster.compendium.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.ui.compose.ColorState
import br.alexandregpereira.hunter.ui.compose.MonsterImage
import br.alexandregpereira.hunter.ui.compose.MonsterImageState
import br.alexandregpereira.hunter.ui.compose.MonsterTypeState
import br.alexandregpereira.hunter.ui.compose.animatePressed
import br.alexandregpereira.hunter.ui.theme.HunterTheme

@Composable
fun MonsterCard(
    monsterCardState: MonsterCardState,
    modifier: Modifier = Modifier,
    onCLick: () -> Unit = {},
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale = animatePressed(pressed = isPressed)

    Column(
        modifier
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onCLick
            )
    ) {
        MonsterImage(monsterCardState.imageState)

        Text(
            text = monsterCardState.name,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 4.dp, start = 4.dp, end = 4.dp)
        )
    }
}

data class MonsterCardState(
    val index: String,
    val name: String,
    val imageState: MonsterImageState,
)

@Preview
@Composable
private fun MonsterCardPreview() {
    HunterTheme {
        val imageState = MonsterImageState(
            url = "asdasdas",
            backgroundColor = ColorState("#ffe3ee", ""),
            challengeRating = 18f,
            type = MonsterTypeState.ABERRATION
        )
        val state = MonsterCardState(
            index = "Monster of the Monsters",
            name = "Monster of the Monsters",
            imageState = imageState
        )
        MonsterCard(state)
    }
}
