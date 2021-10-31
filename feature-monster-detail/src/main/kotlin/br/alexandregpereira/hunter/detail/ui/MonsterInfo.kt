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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.detail.R
import br.alexandregpereira.hunter.ui.compose.ColorState
import br.alexandregpereira.hunter.ui.compose.MonsterImageState
import br.alexandregpereira.hunter.ui.compose.MonsterTypeState
import br.alexandregpereira.hunter.ui.theme.HunterTheme

@Composable
fun MonsterInfo(
    monster: MonsterState,
    modifier: Modifier = Modifier,
    alpha: Float = 1f,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) = Column(
    modifier
        .alpha(alpha)
        .fillMaxWidth()
        .background(
            color = MaterialTheme.colors.surface
        )
) {
    BlockSection { StatsBlock(stats = monster.stats) }
    BlockSection { SpeedBlock(speed = monster.speed) }
    BlockSection { AbilityScoreBlock(abilityScores = monster.abilityScores) }
    OptionalBlockSection(monster.savingThrows) {
        ProficiencyBlock(
            proficiencies = it,
            title = stringResource(R.string.monster_detail_saving_throws)
        )
    }
    OptionalBlockSection(monster.skills) {
        ProficiencyBlock(
            proficiencies = it,
            title = stringResource(R.string.monster_detail_skills)
        )
    }
    OptionalBlockSection(monster.damageVulnerabilities) {
        DamageVulnerabilitiesBlock(damages = it)
    }
    OptionalBlockSection(monster.damageResistances) {
        DamageResistancesBlock(damages = it)
    }
    OptionalBlockSection(monster.damageImmunities) {
        DamageImmunitiesBlock(damages = it)
    }
    OptionalBlockSection(monster.conditionImmunities) {
        ConditionBlock(conditions = it)
    }
    OptionalBlockSection(monster.senses) { SensesBlock(senses = it) }
    OptionalBlockSection(monster.languages) {
        TextBlock(
            title = stringResource(R.string.monster_detail_languages),
            text = it
        )
    }
    OptionalBlockSection(monster.specialAbilities) { SpecialAbilityBlock(specialAbilities = it) }
    BlockSection { ActionBlock(actions = monster.actions) }

    Spacer(
        modifier = Modifier
            .height(contentPadding.calculateBottomPadding())
            .fillMaxWidth()
    )
}

@Composable
private fun <T> ColumnScope.OptionalBlockSection(
    value: List<T>,
    content: @Composable ColumnScope.(List<T>) -> Unit,
) {
    if (value.isEmpty()) return
    BlockSection {
        content(value)
    }
}

@Composable
private fun ColumnScope.OptionalBlockSection(
    value: String,
    content: @Composable ColumnScope.(String) -> Unit,
) {
    if (value.trim().isEmpty()) return
    BlockSection {
        content(value)
    }
}

@Composable
private fun ColumnScope.BlockSection(
    content: @Composable ColumnScope.() -> Unit,
) {
    Spacer(
        modifier = Modifier
            .height(1.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colors.background)
    )

    content()
}

@Preview
@Composable
private fun MonsterInfoPreview() {
    HunterTheme {
        MonsterInfo(
            MonsterState(
                index = "sda",
                name = "Teste dos tes",
                imageState = MonsterImageState(
                    url = "",
                    type = MonsterTypeState.ABERRATION,
                    challengeRating = 0.0f,
                    backgroundColor = ColorState(light = "", dark = ""),
                    isHorizontal = false
                ),
                subtype = null,
                group = null,
                subtitle = "asdasd asdasdas asdasdasd",
                size = "",
                alignment = "",
                stats = StatsState(armorClass = 0, hitPoints = 0, hitDice = ""),
                speed = SpeedState(
                    hover = true, values = (0..6).map {
                        SpeedValueState(
                            type = SpeedTypeState.WALK,
                            valueFormatted = "10m"
                        )
                    }
                ),
                abilityScores = (0..5).map {
                    AbilityScoreState(
                        name = "CHARISMA",
                        value = 0,
                        modifier = 0
                    )
                },
                savingThrows = listOf(),
                skills = listOf(),
                damageVulnerabilities = listOf(),
                damageResistances = listOf(),
                damageImmunities = listOf(),
                conditionImmunities = listOf(),
                senses = listOf(),
                languages = "",
                specialAbilities = listOf(),
                actions = listOf()
            )
        )
    }
}