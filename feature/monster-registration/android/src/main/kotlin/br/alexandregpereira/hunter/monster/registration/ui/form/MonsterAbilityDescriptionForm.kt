package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.domain.model.AbilityDescription
import br.alexandregpereira.hunter.monster.registration.ui.changeAt
import br.alexandregpereira.hunter.ui.compose.AppTextField
import br.alexandregpereira.hunter.ui.compose.Form

@Composable
internal fun MonsterAbilityDescriptionForm(
    title: String,
    abilityDescriptions: List<AbilityDescription>,
    modifier: Modifier = Modifier,
    onChanged: (List<AbilityDescription>) -> Unit = {},
    content: @Composable (Int) -> Unit = { },
) = Form(modifier, title) {
    val newAbilityDescriptions = abilityDescriptions.toMutableList()
    abilityDescriptions.forEachIndexed { index, abilityDescription ->
        AppTextField(
            text = abilityDescription.name,
            label = "Name",
            onValueChange = { newValue ->
                onChanged(newAbilityDescriptions.changeAt(index) { copy(name = newValue) })
            }
        )

        AppTextField(
            text = abilityDescription.description,
            label = "Description",
            multiline = true,
            onValueChange = { newValue ->
                onChanged(newAbilityDescriptions.changeAt(index) { copy(name = newValue) })
            }
        )

        content(index)

        AddButton()
    }

    if (abilityDescriptions.isEmpty()) {
        AddButton()
    }
}
