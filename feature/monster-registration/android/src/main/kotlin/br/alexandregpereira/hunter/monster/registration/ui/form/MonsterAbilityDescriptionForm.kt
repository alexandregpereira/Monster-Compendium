package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import br.alexandregpereira.hunter.domain.model.AbilityDescription
import br.alexandregpereira.hunter.monster.registration.R
import br.alexandregpereira.hunter.monster.registration.ui.changeAt
import br.alexandregpereira.hunter.ui.compose.AppTextField
import br.alexandregpereira.hunter.ui.compose.Form

@Composable
internal fun MonsterAbilityDescriptionForm(
    title: String,
    abilityDescriptions: List<AbilityDescription>,
    addText: String,
    removeText: String,
    modifier: Modifier = Modifier,
    onChanged: (List<AbilityDescription>) -> Unit = {},
) = Form(modifier, title) {
    val newAbilityDescriptions = abilityDescriptions.toMutableList()
    FormItems(
        items = newAbilityDescriptions,
        addText = addText,
        removeText = removeText,
        createNew = { AbilityDescription.create() },
        onChanged = onChanged
    ) { index, abilityDescription ->
        AppTextField(
            text = abilityDescription.name,
            label = stringResource(R.string.monster_registration_name),
            onValueChange = { newValue ->
                onChanged(newAbilityDescriptions.changeAt(index) { copy(name = newValue) })
            }
        )

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
