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

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import br.alexandregpereira.hunter.detail.R
import br.alexandregpereira.hunter.domain.model.Condition
import br.alexandregpereira.hunter.domain.model.ConditionType

@Composable
fun ConditionBlock(
    conditions: List<Condition>,
    modifier: Modifier = Modifier
) = Block(
    title = stringResource(R.string.monster_detail_condition_immunities),
    modifier = modifier
) {

    ConditionGrid(conditions)
}

@Composable
fun ConditionGrid(
    conditions: List<Condition>
) = Grid {

    conditions.forEach { condition ->
        val iconRes = condition.type.getIconRes()
        IconInfo(
            title = condition.name,
            painter = painterResource(iconRes)
        )
    }
}

private fun ConditionType.getIconRes(): Int {
    return when (this) {
        ConditionType.BLINDED -> R.drawable.ic_blinded
        ConditionType.CHARMED -> R.drawable.ic_charmed
        ConditionType.DEAFENED -> R.drawable.ic_deafened
        ConditionType.EXHAUSTION -> R.drawable.ic_exhausted
        ConditionType.FRIGHTENED -> R.drawable.ic_frightened
        ConditionType.GRAPPLED -> R.drawable.ic_grappled
        ConditionType.PARALYZED -> R.drawable.ic_paralyzed
        ConditionType.PETRIFIED -> R.drawable.ic_petrified
        ConditionType.POISONED -> R.drawable.ic_poison
        ConditionType.PRONE -> R.drawable.ic_prone
        ConditionType.RESTRAINED -> R.drawable.ic_restrained
        ConditionType.STUNNED -> R.drawable.ic_stuned
        ConditionType.UNCONSCIOUS -> R.drawable.ic_unconscious
    }
}
