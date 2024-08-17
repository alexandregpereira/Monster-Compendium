package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.foundation.lazy.LazyListScope
import br.alexandregpereira.hunter.monster.registration.SpeedValueState
import br.alexandregpereira.hunter.monster.registration.ui.changeAt
import br.alexandregpereira.hunter.monster.registration.ui.strings
import br.alexandregpereira.hunter.ui.compose.AppTextField
import br.alexandregpereira.hunter.ui.compose.PickerField
import br.alexandregpereira.hunter.ui.compose.form.FormItems
import br.alexandregpereira.hunter.ui.compose.form.FormLazy
import br.alexandregpereira.hunter.ui.compose.form.formItem

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
            addText = { strings.add },
            removeText = { strings.remove },
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
