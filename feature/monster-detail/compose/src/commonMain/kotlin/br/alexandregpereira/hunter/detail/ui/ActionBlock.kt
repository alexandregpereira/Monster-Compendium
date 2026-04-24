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

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.domain.model.ConditionType
import br.alexandregpereira.hunter.domain.model.DamageType
import br.alexandregpereira.hunter.monster.detail.AbilityDescriptionState
import br.alexandregpereira.hunter.monster.detail.ActionState
import br.alexandregpereira.hunter.monster.detail.ConditionState
import br.alexandregpereira.hunter.monster.detail.DamageDiceState
import br.alexandregpereira.hunter.monster.detail.DamageState
import br.alexandregpereira.hunter.monster.detail.ProficiencyState
import br.alexandregpereira.hunter.ui.theme.HunterTheme
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun ActionBlock(
    actions: List<ActionState>,
    modifier: Modifier = Modifier,
    title: String = strings.actions,
    onConditionClicked: (String) -> Unit = {},
) = AbilityDescriptionBlock(
    title = title,
    abilityDescriptions = actions.map { it.abilityDescription },
    modifier = modifier
) { index ->

    val attackBonus = actions[index].attackBonus
    val damageDices = actions[index].damageDices
    if (attackBonus == null
        && damageDices.isEmpty()
        && actions[index].abilityDescription.savingThrows.isEmpty()
        && actions[index].abilityDescription.conditions.isEmpty()
    ) {
        return@AbilityDescriptionBlock
    }

    ActionDamageGrid(
        attackBonus = actions[index].attackBonus,
        damageDices = actions[index].damageDices,
        savingThrows = actions[index].abilityDescription.savingThrows,
        conditions = actions[index].abilityDescription.conditions,
        modifier = Modifier.padding(top = 16.dp),
        onConditionClicked = onConditionClicked,
    )
}

@Composable
internal fun ActionDamageGrid(
    attackBonus: Int?,
    damageDices: List<DamageDiceState>,
    savingThrows: List<ProficiencyState>,
    conditions: List<ConditionState>,
    modifier: Modifier = Modifier,
    onConditionClicked: (String) -> Unit = {},
) = Grid(modifier = modifier) {

    val iconSize = 48.dp
    attackBonus.takeUnless { it == 0 }?.let {
        Bonus(
            value = it,
            name = strings.attack,
            iconSize = iconSize,
            valueTextSize = 16.sp,
            modifier = Modifier
                .width(GridItemWidth)
                .padding(horizontal = 8.dp),
        )
    }

    damageDices.forEach { damageDice ->
        val iconRes = damageDice.damage.type.toIcon()
        if (iconRes != null) {
            IconInfo(
                title = damageDice.dice,
                painter = painterResource(iconRes),
                iconColor = damageDice.damage.type.getIconColor(),
                iconAlpha = 1f,
                iconSize = iconSize,
                modifier = Modifier
                    .width(GridItemWidth)
                    .padding(horizontal = 8.dp),
            )
        }
    }

    savingThrows.forEach { savingThrow ->
        DifficultyClass(
            value = savingThrow.modifier,
            name = savingThrow.name,
            iconSize = iconSize,
            modifier = Modifier
                .width(GridItemWidth)
                .padding(horizontal = 8.dp),
        )
    }

    conditions.forEach { condition ->
        Condition(
            condition = condition,
            iconSize = iconSize,
            modifier = Modifier
                .width(GridItemWidth)
                .padding(horizontal = 8.dp),
            onConditionClicked = onConditionClicked,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ActionBlockPreview() = HunterTheme {
    val actions = listOf(
        ActionState(
            damageDices = listOf(
                DamageDiceState(
                    dice = "1d6",
                    damage = DamageState(
                        index = "acid",
                        type = DamageType.ACID,
                        name = "Acid",
                    )
                ),
                DamageDiceState(
                    dice = "100 (20d10+50)",
                    damage = DamageState(
                        index = "fire",
                        type = DamageType.FIRE,
                        name = "Fogo",
                    )
                ),
            ),
            attackBonus = 10,
            abilityDescription = AbilityDescriptionState(
                name = "Melee Attack",
                description = "Melee attack description",
                savingThrows = listOf(
                    ProficiencyState(
                        index = "strength",
                        modifier = 21,
                        name = "Strength"
                    ),
                    ProficiencyState(
                        index = "strength",
                        modifier = 21,
                        name = "Constitution"
                    ),
                    ProficiencyState(
                        index = "strength",
                        modifier = 21,
                        name = "Constitution"
                    ),
                    ProficiencyState(
                        index = "strength",
                        modifier = 21,
                        name = "Constitution"
                    ),
                ),
                conditions = listOf(
                    ConditionState(
                        index = "blinded",
                        type = ConditionType.RESTRAINED,
                        name = "Blinded"
                    )
                )
            )
        )
    )
    ActionBlock(
        title = "Actions",
        actions = actions,
    )
}
