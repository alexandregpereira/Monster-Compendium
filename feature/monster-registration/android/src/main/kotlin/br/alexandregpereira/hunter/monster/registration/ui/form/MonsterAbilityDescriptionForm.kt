package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.domain.model.AbilityDescription
import br.alexandregpereira.hunter.ui.compose.Form
import br.alexandregpereira.hunter.ui.compose.FormField
import br.alexandregpereira.hunter.ui.compose.ScreenHeader

@Composable
internal fun MonsterAbilityDescriptionForm(
    title: String,
    abilityDescriptions: List<AbilityDescription>,
    modifier: Modifier = Modifier,
    onChanged: (List<AbilityDescription>) -> Unit = {},
    getAdditionalFields: @Composable (Int) -> List<FormField> = { emptyList() },
    onAdditionalFieldChanged: (Int, FormField) -> Unit = { _, _ -> },
) {
    Column(modifier) {
        ScreenHeader(title = title)

        abilityDescriptions.forEachIndexed { index, abilityDescription ->
            Spacer(modifier = Modifier.padding(vertical = 16.dp))

            val id = abilityDescription.name + abilityDescription.description
            val newFields = getAdditionalFields(index)
            Form(
                formFields = listOf(
                    FormField.Text(
                        key = "name",
                        label = "Name",
                        value = abilityDescription.name,
                    ),
                    FormField.Text(
                        key = "description",
                        label = "Description",
                        value = abilityDescription.description,
                        multiline = true,
                    )
                ) + newFields,
                onFormChanged = { field ->
                    when (field.key) {
                        "name" -> onChanged(
                            abilityDescriptions.map {
                                if (it.name + it.description == id) {
                                    it.copy(name = field.stringValue)
                                } else {
                                    it
                                }
                            }
                        )

                        "description" -> onChanged(
                            abilityDescriptions.map {
                                if (it.name + it.description == id) {
                                    it.copy(description = field.stringValue)
                                } else {
                                    it
                                }
                            }
                        )

                        else -> onAdditionalFieldChanged(index, field)
                    }
                }
            )
        }
    }
}