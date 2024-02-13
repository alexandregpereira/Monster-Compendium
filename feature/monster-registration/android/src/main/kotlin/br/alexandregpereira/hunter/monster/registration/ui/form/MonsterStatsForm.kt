package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.foundation.lazy.LazyListScope
import br.alexandregpereira.hunter.monster.registration.StatsState
import br.alexandregpereira.hunter.monster.registration.ui.strings
import br.alexandregpereira.hunter.ui.compose.AppTextField

@Suppress("FunctionName")
internal fun LazyListScope.MonsterStatsForm(
    stats: StatsState,
    onChanged: (StatsState) -> Unit = {}
) {
    val key = "stats"
    FormLazy(
        key = key,
        title = { strings.stats },
    ) {
        formItem(key = "$key-armorClass") {
            AppTextField(
                value = stats.armorClass,
                label = strings.armorClass,
                onValueChange = { newValue ->
                    onChanged(stats.copy(armorClass = newValue))
                }
            )
        }
        formItem(key = "$key-hitPoints") {
            AppTextField(
                value = stats.hitPoints,
                label = strings.hitPoints,
                onValueChange = { newValue ->
                    onChanged(stats.copy(hitPoints = newValue))
                }
            )
        }
        formItem(key = "$key-hitDice") {
            AppTextField(
                text = stats.hitDice,
                label = strings.hitDice,
                onValueChange = { newValue ->
                    onChanged(stats.copy(hitDice = newValue))
                }
            )
        }
    }
}