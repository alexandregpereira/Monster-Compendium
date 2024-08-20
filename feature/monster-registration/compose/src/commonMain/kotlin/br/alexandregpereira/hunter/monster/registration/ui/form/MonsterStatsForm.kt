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

package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.foundation.lazy.LazyListScope
import br.alexandregpereira.hunter.monster.registration.StatsState
import br.alexandregpereira.hunter.monster.registration.ui.strings
import br.alexandregpereira.hunter.ui.compose.AppTextField

@Suppress("FunctionName")
internal fun LazyListScope.MonsterStatsForm(
    keys: Iterator<String>,
    stats: StatsState,
    onChanged: (StatsState) -> Unit = {}
) = FormLazy(
    titleKey = keys.next(),
    title = { strings.stats },
) {
    formItem(key = keys.next()) {
        AppTextField(
            value = stats.armorClass,
            label = strings.armorClass,
            onValueChange = { newValue ->
                onChanged(stats.copy(armorClass = newValue))
            }
        )
    }
    formItem(key = keys.next()) {
        AppTextField(
            value = stats.hitPoints,
            label = strings.hitPoints,
            onValueChange = { newValue ->
                onChanged(stats.copy(hitPoints = newValue))
            }
        )
    }
    formItem(key = keys.next()) {
        AppTextField(
            text = stats.hitDice,
            label = strings.hitDice,
            onValueChange = { newValue ->
                onChanged(stats.copy(hitDice = newValue))
            }
        )
    }
}