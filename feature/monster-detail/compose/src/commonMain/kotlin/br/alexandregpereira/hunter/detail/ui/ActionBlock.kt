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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.monster.detail.ActionState
import br.alexandregpereira.hunter.monster.detail.DamageDiceState

@Composable
internal fun ActionBlock(
    actions: List<ActionState>,
    modifier: Modifier = Modifier,
    title: String = strings.actions,
) = AbilityDescriptionBlock(
    title = title,
    abilityDescriptions = actions.map { it.abilityDescription },
    modifier = modifier
) { index ->

    val attackBonus = actions[index].attackBonus
    val damageDices = actions[index].damageDices
    if (attackBonus == null && damageDices.isEmpty()) return@AbilityDescriptionBlock

    ActionDamageGrid(
        attackBonus = actions[index].attackBonus,
        damageDices = actions[index].damageDices,
        modifier = Modifier.padding(top = 16.dp)
    )
}

@Composable
private fun ActionDamageGrid(
    attackBonus: Int?,
    damageDices: List<DamageDiceState>,
    modifier: Modifier = Modifier
) = Grid(modifier = modifier) {

    val iconSize = 48.dp
    attackBonus.takeUnless { it == 0 }?.let {
        Bonus(value = it, name = strings.attack, iconSize = iconSize)
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
            )
        }
    }
}
