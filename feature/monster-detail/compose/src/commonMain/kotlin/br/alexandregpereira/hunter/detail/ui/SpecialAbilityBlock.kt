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
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.monster.detail.AbilityDescriptionState

@Composable
internal fun SpecialAbilityBlock(
    specialAbilities: List<AbilityDescriptionState>,
    modifier: Modifier = Modifier,
    onConditionClicked: (String) -> Unit = {},
) = AbilityDescriptionBlock(
    title = strings.specialAbilities,
    abilityDescriptions = specialAbilities,
    modifier = modifier
) { index ->
    if (specialAbilities[index].savingThrows.isEmpty()
        && specialAbilities[index].conditions.isEmpty()
    ) {
        return@AbilityDescriptionBlock
    }
    ActionDamageGrid(
        attackBonus = null,
        damageDices = emptyList(),
        savingThrows = specialAbilities[index].savingThrows,
        conditions = specialAbilities[index].conditions,
        modifier = Modifier.padding(top = 16.dp),
        onConditionClicked = onConditionClicked,
    )
}
