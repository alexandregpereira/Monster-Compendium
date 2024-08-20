/*
 * Copyright (C) 2024 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.monster.detail.AbilityScoreState
import br.alexandregpereira.hunter.ui.compose.Window
import br.alexandregpereira.hunter.ui.theme.HunterTheme

@Composable
internal fun AbilityScoreBlock(
    abilityScores: List<AbilityScoreState>,
    modifier: Modifier = Modifier,
) = Block(
    title = strings.abilityScores,
    modifier = modifier,
) {

    AbilityScoreGrid(abilityScores)
}

@Composable
internal fun AbilityScoreGrid(
    abilityScores: List<AbilityScoreState>,
    modifier: Modifier = Modifier,
) = Column(modifier) {

    Grid {
        abilityScores.getOrNull(0)?.let {
            AbilityScore(abilityScore = it)
        }
        abilityScores.getOrNull(1)?.let {
            AbilityScore(abilityScore = it)
        }
        abilityScores.getOrNull(2)?.let {
            AbilityScore(abilityScore = it)
        }
    }

    Grid(Modifier.padding(top = 24.dp)) {
        abilityScores.getOrNull(3)?.let {
            AbilityScore(abilityScore = it)
        }
        abilityScores.getOrNull(4)?.let {
            AbilityScore(abilityScore = it)
        }
        abilityScores.getOrNull(5)?.let {
            AbilityScore(abilityScore = it)
        }
    }
}

@Preview
@Composable
internal fun AbilityScoreBlockPreview() = HunterTheme {
    AbilityScoreBlock(
        abilityScores = (0..5).map {
            AbilityScoreState(
                value = 20,
                modifier = 5,
                name = "Charisma",
            )
        }
    )
}

@Preview
@Composable
internal fun AbilityScoreGridPreview() = Window {
    AbilityScoreGrid(
        abilityScores = (0..5).map {
            AbilityScoreState(
                value = 20,
                modifier = 5,
                name = "Charisma",
            )
        }
    )
}
