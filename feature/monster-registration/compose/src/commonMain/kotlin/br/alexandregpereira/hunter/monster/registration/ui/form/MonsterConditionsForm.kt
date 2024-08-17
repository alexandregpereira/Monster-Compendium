package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import br.alexandregpereira.hunter.monster.registration.ConditionState
import br.alexandregpereira.hunter.monster.registration.ui.changeAt
import br.alexandregpereira.hunter.monster.registration.ui.strings
import br.alexandregpereira.hunter.ui.compose.PickerField
import br.alexandregpereira.hunter.ui.compose.form.FormItems
import br.alexandregpereira.hunter.ui.compose.form.FormLazy
import br.alexandregpereira.hunter.ui.compose.form.formItem

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
            addText = { strings.add },
            removeText = { strings.remove },
            onChanged = onChanged
        ) { i, condition ->
            formItem(key = keys.next()) {
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
