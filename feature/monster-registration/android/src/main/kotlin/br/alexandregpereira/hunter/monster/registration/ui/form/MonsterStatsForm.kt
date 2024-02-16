package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.foundation.lazy.LazyListScope
import br.alexandregpereira.hunter.monster.registration.StatsState
import br.alexandregpereira.hunter.monster.registration.ui.strings
import br.alexandregpereira.hunter.ui.compose.AppTextField

@Suppress("FunctionName")
internal fun LazyListScope.MonsterStatsForm(
    keys: Iterator<String>,
    stats: StatsState,
    onChanged: (StatsState) -> Unit = {}
) = FormLazy(
    titleKey = keys.next(),
    title = { strings.stats },
) {
    formItem(key = keys.next()) {
        AppTextField(
            value = stats.armorClass,
            label = strings.armorClass,
            onValueChange = { newValue ->
                onChanged(stats.copy(armorClass = newValue))
            }
        )
    }
    formItem(key = keys.next()) {
        AppTextField(
            value = stats.hitPoints,
            label = strings.hitPoints,
            onValueChange = { newValue ->
                onChanged(stats.copy(hitPoints = newValue))
            }
        )
    }
    formItem(key = keys.next()) {
        AppTextField(
            text = stats.hitDice,
            label = strings.hitDice,
            onValueChange = { newValue ->
                onChanged(stats.copy(hitDice = newValue))
            }
        )
    }
}