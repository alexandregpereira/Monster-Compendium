package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.ui.compose.Form
import br.alexandregpereira.hunter.ui.compose.FormField

@Composable
internal fun MonsterStatsForm(
    monster: Monster,
    modifier: Modifier = Modifier,
    onMonsterChanged: (Monster) -> Unit = {}
) {
    Form(
        modifier = modifier,
        title = "Stats",
        formFields = listOf(
            FormField.Number(
                key = "armorClass",
                label = "Armor Class",
                value = monster.stats.armorClass,
            ),
            FormField.Number(
                key = "hitPoints",
                label = "Hit Points",
                value = monster.stats.hitPoints,
            ),
            FormField.Text(
                key = "hitDice",
                label = "Hit Dice",
                value = monster.stats.hitDice,
            ),
        ),
        onFormChanged = { field ->
            when (field.key) {
                "armorClass" -> onMonsterChanged(
                    monster.copy(
                        stats = monster.stats.copy(
                            armorClass = field.intValue
                        )
                    )
                )

                "hitPoints" -> onMonsterChanged(
                    monster.copy(
                        stats = monster.stats.copy(
                            hitPoints = field.intValue
                        )
                    )
                )

                "hitDice" -> onMonsterChanged(
                    monster.copy(stats = monster.stats.copy(hitDice = field.stringValue))
                )
            }
        },
    )
}