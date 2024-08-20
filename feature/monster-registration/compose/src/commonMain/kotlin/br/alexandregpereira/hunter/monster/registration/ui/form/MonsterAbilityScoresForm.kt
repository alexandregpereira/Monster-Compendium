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
import androidx.compose.ui.util.fastForEachIndexed
import br.alexandregpereira.hunter.monster.registration.AbilityScoreState
import br.alexandregpereira.hunter.monster.registration.ui.changeAt
import br.alexandregpereira.hunter.monster.registration.ui.strings
import br.alexandregpereira.hunter.ui.compose.AppTextField

@Suppress("FunctionName")
internal fun LazyListScope.MonsterAbilityScoresForm(
    keys: Iterator<String>,
    abilityScores: List<AbilityScoreState>,
    onChanged: (List<AbilityScoreState>) -> Unit = {}
) = FormLazy(
    titleKey = keys.next(),
    title = { strings.abilityScores },
) {
    val newAbilityScores = abilityScores.toMutableList()
    abilityScores.fastForEachIndexed { i, abilityScore ->
        formItem(key = keys.next()) {
            AppTextField(
                value = abilityScore.value,
                label = abilityScore.name,
                onValueChange = { newValue ->
                    onChanged(newAbilityScores.changeAt(i) { copy(value = newValue) })
                }
            )
        }
    }
}
