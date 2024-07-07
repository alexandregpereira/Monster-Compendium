package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.foundation.lazy.LazyListScope
import br.alexandregpereira.hunter.monster.registration.SavingThrowState
import br.alexandregpereira.hunter.monster.registration.ui.changeAt
import br.alexandregpereira.hunter.monster.registration.ui.strings
import br.alexandregpereira.hunter.ui.compose.AppTextField
import br.alexandregpereira.hunter.ui.compose.PickerField

@Suppress("FunctionName")
internal fun LazyListScope.MonsterSavingThrowsForm(
    keys: Iterator<String>,
    savingThrows: List<SavingThrowState>,
    onChanged: (List<SavingThrowState>) -> Unit = {}
) {
    val mutableSavingThrows = savingThrows.toMutableList()

    FormLazy(
        titleKey = keys.next(),
        title = { strings.savingThrows },
    ) {

        FormItems(
            keys = keys,
            items = mutableSavingThrows,
            createNew = { SavingThrowState() },
            onChanged = onChanged
        ) { i, savingThrow ->
            formItem(key = keys.next()) {
                PickerField(
                    value = savingThrow.name,
                    label = strings.name,
                    options = savingThrow.filteredOptions,
                    onValueChange = { optionIndex ->
                        onChanged(
                            mutableSavingThrows.changeAt(i) {
                                copy(selectedIndex = savingThrow.selectedIndex(optionIndex))
                            }
                        )
                    }
                )
            }

            formItem(key = keys.next()) {
                AppTextField(
                    value = savingThrow.modifier,
                    label = savingThrow.name,
                    onValueChange = { newValue ->
                        onChanged(
                            mutableSavingThrows.changeAt(i) {
                                copy(modifier = newValue)
                            }
                        )
                    }
                )
            }
        }
    }
}
