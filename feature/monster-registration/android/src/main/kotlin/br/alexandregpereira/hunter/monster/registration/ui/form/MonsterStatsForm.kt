package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.res.stringResource
import br.alexandregpereira.hunter.domain.model.Stats
import br.alexandregpereira.hunter.monster.registration.R
import br.alexandregpereira.hunter.ui.compose.AppTextField

@Suppress("FunctionName")
internal fun LazyListScope.MonsterStatsForm(
    stats: Stats,
    onChanged: (Stats) -> Unit = {}
) {
    val key = "stats"
    FormLazy(
        key = key,
        title = { stringResource(R.string.monster_registration_stats) },
    ) {
        formItem(key = "$key-armorClass") {
            AppTextField(
                value = stats.armorClass,
                label = stringResource(R.string.monster_registration_armor_class),
                onValueChange = { newValue ->
                    onChanged(stats.copy(armorClass = newValue))
                }
            )
        }
        formItem(key = "$key-hitPoints") {
            AppTextField(
                value = stats.hitPoints,
                label = stringResource(R.string.monster_registration_hit_points),
                onValueChange = { newValue ->
                    onChanged(stats.copy(hitPoints = newValue))
                }
            )
        }
        formItem(key = "$key-hitDice") {
            AppTextField(
                text = stats.hitDice,
                label = stringResource(R.string.monster_registration_hit_dice),
                onValueChange = { newValue ->
                    onChanged(stats.copy(hitDice = newValue))
                }
            )
        }
    }
}