package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.foundation.lazy.LazyListScope
import br.alexandregpereira.hunter.monster.registration.SpeedValueState
import br.alexandregpereira.hunter.monster.registration.ui.changeAt
import br.alexandregpereira.hunter.monster.registration.ui.strings
import br.alexandregpereira.hunter.ui.compose.AppTextField
import br.alexandregpereira.hunter.ui.compose.PickerField

@Suppress("FunctionName")
internal fun LazyListScope.MonsterSpeedValuesForm(
    speedValueStates: List<SpeedValueState>,
    onMonsterChanged: (List<SpeedValueState>) -> Unit = {}
) {
    val newSpeedValues = speedValueStates.toMutableList()
    val key = "speed"

    FormLazy(
        key = key,
        title = { strings.speed },
    ) {
        FormItems(
            key = key,
            items = newSpeedValues,
            createNew = { SpeedValueState() },
            onChanged = {
                onMonsterChanged(it)
            }
        ) { index, speedValue ->
            formItem(key = "$key-name-${speedValue.key}") {
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

            formItem(key = "$key-value-${speedValue.key}") {
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
