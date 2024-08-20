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
import br.alexandregpereira.hunter.monster.detail.ProficiencyState

@Composable
internal fun ProficiencyBlock(
    title: String,
    proficiencies: List<ProficiencyState>,
    modifier: Modifier = Modifier
) = Block(modifier = modifier, title = title) {
    ProficiencyGrid(proficiencies)
}

@Composable
private fun ProficiencyGrid(
    proficiencies: List<ProficiencyState>,
) = Grid {

    proficiencies.forEach { proficiency ->
        Bonus(
            value = proficiency.modifier,
            name = proficiency.name,
            modifier = Modifier.width(GridItemWidth),
        )
    }
}
