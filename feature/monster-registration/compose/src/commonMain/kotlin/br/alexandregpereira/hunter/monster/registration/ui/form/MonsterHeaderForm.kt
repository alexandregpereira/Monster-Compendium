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
import br.alexandregpereira.hunter.monster.registration.MonsterInfoState
import br.alexandregpereira.hunter.monster.registration.ui.strings
import br.alexandregpereira.hunter.ui.compose.AppKeyboardType
import br.alexandregpereira.hunter.ui.compose.AppTextField
import br.alexandregpereira.hunter.ui.compose.PickerField

@Suppress("FunctionName")
internal fun LazyListScope.MonsterHeaderForm(
    keys: Iterator<String>,
    infoState: MonsterInfoState,
    onMonsterChanged: (MonsterInfoState) -> Unit = {}
) {
    FormLazy(
        titleKey = keys.next(),
        title = { strings.header }
    ) {
        formItem(key = keys.next()) {
            AppTextField(
                text = infoState.name,
                label = strings.name,
                onValueChange = { onMonsterChanged(infoState.copy(name = it)) }
            )
        }
        formItem(key = keys.next()) {
            AppTextField(
                text = infoState.subtitle,
                label = strings.subtitle,
                onValueChange = { onMonsterChanged(infoState.copy(subtitle = it)) }
            )
        }
        formItem(key = keys.next()) {
            AppTextField(
                text = infoState.group,
                label = strings.group,
                onValueChange = {
                    onMonsterChanged(infoState.copy(group = it.takeUnless { it.isBlank() }.orEmpty()))
                }
            )
        }
        formItem(key = keys.next()) {
            AppTextField(
                text = infoState.challengeRating,
                label = strings.challengeRating,
                keyboardType = AppKeyboardType.DECIMAL,
                onValueChange = {
                    onMonsterChanged(infoState.copy(challengeRating = it))
                }
            )
        }
        formItem(key = keys.next()) {
            PickerField(
                value = infoState.type,
                label = strings.type,
                options = infoState.typeOptions,
                onValueChange = { i ->
                    onMonsterChanged(infoState.copy(typeIndex = i))
                }
            )
        }
    }
}
