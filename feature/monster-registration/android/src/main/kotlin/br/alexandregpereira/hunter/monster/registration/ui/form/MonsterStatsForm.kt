package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.monster.registration.R
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
        title = stringResource(R.string.monster_registration_stats),
        formFields = listOf(
            FormField.Number(
                key = "armorClass",
                label = stringResource(R.string.monster_registration_armor_class),
                value = monster.stats.armorClass,
            ),
            FormField.Number(
                key = "hitPoints",
                label = stringResource(R.string.monster_registration_hit_points),
                value = monster.stats.hitPoints,
            ),
            FormField.Text(
                key = "hitDice",
                label = stringResource(R.string.monster_registration_hit_dice),
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