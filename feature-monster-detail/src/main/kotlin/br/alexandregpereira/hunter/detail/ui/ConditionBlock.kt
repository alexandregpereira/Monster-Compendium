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

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import br.alexandregpereira.hunter.detail.R

@Composable
fun ConditionBlock(
    conditions: List<ConditionState>,
    modifier: Modifier = Modifier
) = Block(
    title = stringResource(R.string.monster_detail_condition_immunities),
    modifier = modifier
) {

    ConditionGrid(conditions)
}

@Composable
private fun ConditionGrid(
    conditions: List<ConditionState>
) = Grid {

    conditions.forEach { condition ->
        val iconRes = condition.type.iconRes
        IconInfo(
            title = condition.name,
            painter = painterResource(iconRes)
        )
    }
}
