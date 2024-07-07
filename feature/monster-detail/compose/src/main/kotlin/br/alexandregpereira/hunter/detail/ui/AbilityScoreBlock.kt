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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.monster.detail.AbilityScoreState
import br.alexandregpereira.hunter.ui.compose.Window
import br.alexandregpereira.hunter.ui.theme.HunterTheme

@Composable
fun AbilityScoreBlock(
    abilityScores: List<AbilityScoreState>,
    modifier: Modifier = Modifier,
) = Block(
    title = strings.abilityScores,
    modifier = modifier,
) {

    AbilityScoreGrid(abilityScores)
}

@Composable
fun AbilityScoreGrid(
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
fun AbilityScoreBlockPreview() = HunterTheme {
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
fun AbilityScoreGridPreview() = Window {
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
