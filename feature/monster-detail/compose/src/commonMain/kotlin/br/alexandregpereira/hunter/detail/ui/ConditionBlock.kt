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

import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import br.alexandregpereira.hunter.monster.detail.ConditionState

@Composable
internal fun ConditionBlock(
    conditions: List<ConditionState>,
    modifier: Modifier = Modifier
) = Block(
    title = strings.conditionImmunities,
    modifier = modifier
) {

    ConditionGrid(conditions)
}

@Composable
private fun ConditionGrid(
    conditions: List<ConditionState>
) = Grid {

    conditions.forEach { condition ->
        val iconRes = condition.type.toIcon()
        IconInfo(
            title = condition.name,
            painter = painterResource(iconRes),
            modifier = Modifier.width(GridItemWidth)
        )
    }
}
