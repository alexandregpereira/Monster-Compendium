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
            FormField(
                key = "armorClass",
                label = "Armor Class",
                value = monster.stats.armorClass.toString(),
            ),
            FormField(
                key = "hitPoints",
                label = "Hit Points",
                value = monster.stats.hitPoints.toString(),
            ),
            FormField(
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
                            armorClass = field.value.toIntOrNull() ?: 0
                        )
                    )
                )

                "hitPoints" -> onMonsterChanged(
                    monster.copy(
                        stats = monster.stats.copy(
                            hitPoints = field.value.toIntOrNull() ?: 0
                        )
                    )
                )

                "hitDice" -> onMonsterChanged(monster.copy(stats = monster.stats.copy(hitDice = field.value)))
            }
        },
    )
}