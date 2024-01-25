package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import br.alexandregpereira.hunter.domain.model.AbilityDescription
import br.alexandregpereira.hunter.monster.registration.R
import br.alexandregpereira.hunter.monster.registration.ui.changeAt
import br.alexandregpereira.hunter.ui.compose.AppTextField

@Suppress("FunctionName")
internal fun LazyListScope.MonsterAbilityDescriptionForm(
    key: String,
    title: @Composable () -> String,
    abilityDescriptions: List<AbilityDescription>,
    addText: @Composable () -> String,
    removeText: @Composable () -> String,
    onChanged: (List<AbilityDescription>) -> Unit = {},
) = FormLazy(key, title) {
    val newAbilityDescriptions = abilityDescriptions.toMutableList()
    FormItems(
        key = key,
        items = newAbilityDescriptions,
        addText = addText,
        removeText = removeText,
        createNew = { AbilityDescription.create() },
        onChanged = onChanged
    ) { index, abilityDescription ->
        formItem(key = "$key-ability-description-name-$index") {
            AppTextField(
                text = abilityDescription.name,
                label = stringResource(R.string.monster_registration_name),
                onValueChange = { newValue ->
                    onChanged(newAbilityDescriptions.changeAt(index) { copy(name = newValue) })
                }
            )
        }

        formItem(key = "$key-ability-description-description-$index") {
            AppTextField(
                text = abilityDescription.description,
                label = stringResource(R.string.monster_registration_description),
                multiline = true,
                onValueChange = { newValue ->
                    onChanged(newAbilityDescriptions.changeAt(index) { copy(description = newValue) })
                }
            )
        }
    }
}
