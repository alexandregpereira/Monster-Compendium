package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import br.alexandregpereira.hunter.monster.registration.ConditionState
import br.alexandregpereira.hunter.monster.registration.ui.changeAt
import br.alexandregpereira.hunter.monster.registration.ui.strings
import br.alexandregpereira.hunter.ui.compose.PickerField

@Suppress("FunctionName")
internal fun LazyListScope.MonsterConditionsForm(
    title: @Composable () -> String,
    conditions: List<ConditionState>,
    onChanged: (List<ConditionState>) -> Unit = {}
) {
    val newConditions = conditions.toMutableList()
    val key = "conditionImmunities"
    FormLazy(
        key = key,
        title = title,
    ) {
        FormItems(
            key = key,
            items = newConditions,
            createNew = { ConditionState() },
            onChanged = onChanged
        ) { i, condition ->
            formItem(key = "$key-name-${condition.key}") {
                PickerField(
                    value = condition.name,
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
        }
    }
}
