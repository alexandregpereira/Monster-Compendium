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
import androidx.compose.runtime.Composable
import br.alexandregpereira.hunter.monster.registration.AbilityDescriptionState
import br.alexandregpereira.hunter.monster.registration.ui.changeAt
import br.alexandregpereira.hunter.monster.registration.ui.strings
import br.alexandregpereira.hunter.ui.compose.AppTextField

@Suppress("FunctionName")
internal fun LazyListScope.MonsterAbilityDescriptionForm(
    keys: Iterator<String>,
    title: @Composable () -> String,
    abilityDescriptions: List<AbilityDescriptionState>,
    addText: @Composable () -> String,
    removeText: @Composable () -> String,
    onChanged: (List<AbilityDescriptionState>) -> Unit = {},
) = FormLazy(keys.next(), title) {
    val newAbilityDescriptions = abilityDescriptions.toMutableList()
    FormItems(
        keys = keys,
        items = newAbilityDescriptions,
        addText = addText,
        removeText = removeText,
        createNew = { AbilityDescriptionState() },
        onChanged = onChanged
    ) { index, abilityDescription ->
        formItem(key = keys.next()) {
            AppTextField(
                text = abilityDescription.name,
                label = strings.name,
                onValueChange = { newValue ->
                    onChanged(newAbilityDescriptions.changeAt(index) { copy(name = newValue) })
                }
            )
        }

        formItem(key = keys.next()) {
            AppTextField(
                text = abilityDescription.description,
                label = strings.description,
                multiline = true,
                onValueChange = { newValue ->
                    onChanged(newAbilityDescriptions.changeAt(index) { copy(description = newValue) })
                }
            )
        }
    }
}
