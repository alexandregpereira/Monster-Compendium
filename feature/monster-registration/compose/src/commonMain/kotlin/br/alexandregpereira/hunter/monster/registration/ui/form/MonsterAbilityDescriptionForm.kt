package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import br.alexandregpereira.hunter.monster.registration.AbilityDescriptionState
import br.alexandregpereira.hunter.monster.registration.ui.changeAt
import br.alexandregpereira.hunter.monster.registration.ui.strings
import br.alexandregpereira.hunter.ui.compose.AppTextField
import br.alexandregpereira.hunter.ui.compose.form.FormItems
import br.alexandregpereira.hunter.ui.compose.form.FormLazy
import br.alexandregpereira.hunter.ui.compose.form.formItem

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
