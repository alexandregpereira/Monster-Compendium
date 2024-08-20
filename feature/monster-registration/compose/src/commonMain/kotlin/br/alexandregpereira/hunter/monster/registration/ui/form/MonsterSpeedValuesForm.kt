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
import br.alexandregpereira.hunter.monster.registration.SpeedValueState
import br.alexandregpereira.hunter.monster.registration.ui.changeAt
import br.alexandregpereira.hunter.monster.registration.ui.strings
import br.alexandregpereira.hunter.ui.compose.AppTextField
import br.alexandregpereira.hunter.ui.compose.PickerField

@Suppress("FunctionName")
internal fun LazyListScope.MonsterSpeedValuesForm(
    keys: Iterator<String>,
    speedValueStates: List<SpeedValueState>,
    onMonsterChanged: (List<SpeedValueState>) -> Unit = {}
) {
    val newSpeedValues = speedValueStates.toMutableList()

    FormLazy(
        titleKey = keys.next(),
        title = { strings.speed },
    ) {
        FormItems(
            keys = keys,
            items = newSpeedValues,
            createNew = { SpeedValueState() },
            onChanged = {
                onMonsterChanged(it)
            }
        ) { index, speedValue ->
            formItem(key = keys.next()) {
                PickerField(
                    value = speedValue.type,
                    label = strings.speedType,
                    options = speedValue.options,
                    onValueChange = { optionIndex ->
                        onMonsterChanged(
                            newSpeedValues.changeAt(index) {
                                copy(typeIndex = optionIndex)
                            }
                        )
                    }
                )
            }

            formItem(key = keys.next()) {
                AppTextField(
                    text = speedValue.value,
                    label = speedValue.type,
                    onValueChange = { newValue ->
                        onMonsterChanged(
                            newSpeedValues.changeAt(index) {
                                copy(value = newValue)
                            }
                        )
                    }
                )
            }
        }
    }
}
