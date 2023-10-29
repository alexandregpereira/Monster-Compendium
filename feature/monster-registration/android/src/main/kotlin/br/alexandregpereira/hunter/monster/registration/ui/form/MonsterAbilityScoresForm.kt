package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.ui.compose.Form
import br.alexandregpereira.hunter.ui.compose.FormField

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
            FormField.Number(
                key = abilityScore.type.name,
                label = abilityScore.type.name,
                value = abilityScore.value,
            )
        },
        onFormChanged = { field ->
            val abilityScores = monster.abilityScores.toMutableList()
            val index = abilityScores.indexOfFirst { it.type.name == field.key }
            if (index != -1) {
                abilityScores[index] =
                    abilityScores[index].copy(value = field.intValue)
                onMonsterChanged(monster.copy(abilityScores = abilityScores))
            }
        },
    )
}
