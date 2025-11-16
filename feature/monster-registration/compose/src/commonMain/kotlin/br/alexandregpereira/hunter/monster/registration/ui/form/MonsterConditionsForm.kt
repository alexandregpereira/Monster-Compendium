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

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.monster.registration.ConditionState
import br.alexandregpereira.hunter.monster.registration.ui.changeAt
import br.alexandregpereira.hunter.monster.registration.ui.strings
import br.alexandregpereira.hunter.ui.compose.AppTextField
import br.alexandregpereira.hunter.ui.compose.PickerField

@Suppress("FunctionName")
internal fun LazyListScope.MonsterConditionsForm(
    keys: Iterator<String>,
    title: @Composable () -> String,
    conditions: List<ConditionState>,
    onChanged: (List<ConditionState>) -> Unit = {}
) {
    val newConditions = conditions.toMutableList()
    FormLazy(
        titleKey = keys.next(),
        title = title,
    ) {
        FormItems(
            keys = keys,
            items = newConditions,
            createNew = { ConditionState() },
            onChanged = onChanged
        ) { i, condition ->
            formItem(key = keys.next()) {
                PickerField(
                    value = condition.typeName,
                    label = strings.conditionType,
                    options = condition.filteredOptions,
                    onValueChange = { optionIndex ->
                        onChanged(
                            newConditions.changeAt(i) {
                                copy(selectedIndex = condition.selectedIndex(optionIndex))
                            }
                        )
                    }
                )
            }
            formItem(key = keys.next()) {
                AppTextField(
                    text = condition.name,
                    label = condition.typeName,
                    onValueChange = { newValue ->
                        onChanged(newConditions.changeAt(i) { copy(name = newValue) })
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
