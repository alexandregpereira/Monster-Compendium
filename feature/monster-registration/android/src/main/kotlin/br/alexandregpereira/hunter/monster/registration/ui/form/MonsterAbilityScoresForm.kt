package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.domain.model.AbilityDescription
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.Proficiency
import br.alexandregpereira.hunter.ui.compose.Form
import br.alexandregpereira.hunter.ui.compose.FormField
import br.alexandregpereira.hunter.ui.compose.ScreenHeader

@Composable
internal fun MonsterAbilityScoresForm(
    monster: Monster,
    modifier: Modifier = Modifier,
    onMonsterChanged: (Monster) -> Unit = {}
) {
    Form(
        modifier = modifier,
        title = "Ability Scores",
        formFields = monster.abilityScores.map { abilityScore ->
            FormField(
                key = abilityScore.type.name,
                label = abilityScore.type.name,
                value = abilityScore.value.toString(),
            )
        },
        onFormChanged = { field ->
            val abilityScores = monster.abilityScores.toMutableList()
            val index = abilityScores.indexOfFirst { it.type.name == field.key }
            if (index != -1) {
                abilityScores[index] =
                    abilityScores[index].copy(value = field.value.toIntOrNull() ?: 0)
                onMonsterChanged(monster.copy(abilityScores = abilityScores))
            }
        },
    )
}

@Composable
internal fun MonsterAbilityDescriptionForm(
    title: String,
    abilityDescriptions: List<AbilityDescription>,
    modifier: Modifier = Modifier,
    onChanged: (List<AbilityDescription>) -> Unit = {}
) {
    Column(modifier) {
        ScreenHeader(title = title)

//        Spacer(modifier = Modifier.padding(top = 24.dp))

        abilityDescriptions.forEach { abilityDescription ->
            Spacer(modifier = Modifier.padding(vertical = 16.dp))

            val id = abilityDescription.name + abilityDescription.description
            Form(
                formFields = listOf(
                    FormField(
                        key = "name",
                        label = "Name",
                        value = abilityDescription.name,
                    ),
                    FormField(
                        key = "description",
                        label = "Description",
                        value = abilityDescription.description,
                    )
                ),
                onFormChanged = { field ->
                    when (field.key) {
                        "name" -> onChanged(
                            abilityDescriptions.map {
                                if (it.name + it.description == id) {
                                    it.copy(name = field.value)
                                } else {
                                    it
                                }
                            }
                        )

                        "description" -> onChanged(
                            abilityDescriptions.map {
                                if (it.name + it.description == id) {
                                    it.copy(description = field.value)
                                } else {
                                    it
                                }
                            }
                        )
                    }
                }
            )
        }
    }
}

@Composable
internal fun MonsterProficiencyForm(
    title: String,
    proficiencies: List<Proficiency>,
    modifier: Modifier = Modifier,
    onChanged: (List<Proficiency>) -> Unit = {}
) {
    Form(
        modifier = modifier,
        title = title,
        formFields = proficiencies.map { proficiency ->
            FormField(
                key = proficiency.index,
                label = proficiency.name,
                value = proficiency.modifier.toString(),
            )
        },
        onFormChanged = { field ->
            val newProficiencies = proficiencies.toMutableList()
            val index = newProficiencies.indexOfFirst { it.index == field.key }
            if (index != -1) {
                newProficiencies[index] =
                    proficiencies[index].copy(modifier = field.value.toIntOrNull() ?: 0)
                onChanged(newProficiencies)
            }
        },
    )
}

@Composable
internal fun MonsterStringValueForm(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    onChanged: (String) -> Unit = {}
) {
    Form(
        modifier = modifier,
        title = title,
        formFields = listOf(
            FormField(
                key = title,
                label = title,
                value = value,
            )
        ),
        onFormChanged = { field ->
            onChanged(field.value)
        },
    )
}