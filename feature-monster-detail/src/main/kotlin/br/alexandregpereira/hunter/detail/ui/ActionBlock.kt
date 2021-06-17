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

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.detail.R

@Composable
fun ActionBlock(
    actions: List<ActionState>,
    modifier: Modifier = Modifier
) = AbilityDescriptionBlock(
    title = stringResource(R.string.monster_detail_actions),
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
    attackBonus?.let {
        Bonus(value = it, name = stringResource(R.string.monster_detail_attack), iconSize = iconSize)
    }

    damageDices.forEach { damageDice ->
        val iconRes = damageDice.damage.type.iconRes
        if (iconRes != null) {
            IconInfo(
                title = damageDice.dice,
                painter = painterResource(iconRes),
                iconColor = damageDice.damage.type.getIconColor(),
                iconAlpha = 1f,
                iconSize = iconSize
            )
        }
    }
}
