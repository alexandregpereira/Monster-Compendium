package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.alexandregpereira.hunter.domain.model.Skill
import br.alexandregpereira.hunter.ui.compose.Form
import br.alexandregpereira.hunter.ui.compose.FormField

@Composable
internal fun MonsterProficiencyForm(
    title: String,
    proficiencies: List<Skill>,
    modifier: Modifier = Modifier,
    onChanged: (List<Skill>) -> Unit = {}
) {
    Form(
        modifier = modifier,
        title = title,
        formFields = proficiencies.map { proficiency ->
            listOf(
                FormField.Text(
                    key = proficiency.index + "-name",
                    label = "Name",
                    value = proficiency.name,
                ),
                FormField.Number(
                    key = proficiency.index + "-modifier",
                    label = proficiency.name,
                    value = proficiency.modifier,
                )
            )
        }.reduceOrNull { acc, texts -> acc + texts } ?: emptyList(),
        onFormChanged = { field ->
            val newProficiencies = proficiencies.toMutableList()
            val index = newProficiencies.indexOfFirst { field.key.startsWith(it.index) }
            if (index != -1) {
                when (field.key) {
                    "${newProficiencies[index].index}-name" -> {
                        newProficiencies[index] =
                            newProficiencies[index].copy(name = field.stringValue)
                    }

                    "${newProficiencies[index].index}-modifier" -> {
                        newProficiencies[index] =
                            newProficiencies[index].copy(modifier = field.intValue)
                    }
                }
                onChanged(newProficiencies)
            }
        },
    )
}
