package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import br.alexandregpereira.hunter.monster.registration.AbilityDescriptionState
import br.alexandregpereira.hunter.monster.registration.ui.changeAt
import br.alexandregpereira.hunter.monster.registration.ui.strings
import br.alexandregpereira.hunter.ui.compose.AppTextField

@Suppress("FunctionName")
internal fun LazyListScope.MonsterAbilityDescriptionForm(
    key: String,
    title: @Composable () -> String,
    abilityDescriptions: List<AbilityDescriptionState>,
    addText: @Composable () -> String,
    removeText: @Composable () -> String,
    onChanged: (List<AbilityDescriptionState>) -> Unit = {},
) = FormLazy(key, title) {
    val newAbilityDescriptions = abilityDescriptions.toMutableList()
    FormItems(
        key = key,
        items = newAbilityDescriptions,
        addText = addText,
        removeText = removeText,
        createNew = { AbilityDescriptionState() },
        onChanged = onChanged
    ) { index, abilityDescription ->
        formItem(key = "$key-ability-description-name-${abilityDescription.key}") {
            AppTextField(
                text = abilityDescription.name,
                label = strings.name,
                onValueChange = { newValue ->
                    onChanged(newAbilityDescriptions.changeAt(index) { copy(name = newValue) })
                }
            )
        }

        formItem(key = "$key-ability-description-description-${abilityDescription.key}") {
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
