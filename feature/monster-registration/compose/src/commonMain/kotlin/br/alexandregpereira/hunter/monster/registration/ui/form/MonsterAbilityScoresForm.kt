package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.util.fastForEachIndexed
import br.alexandregpereira.hunter.monster.registration.AbilityScoreState
import br.alexandregpereira.hunter.monster.registration.ui.changeAt
import br.alexandregpereira.hunter.monster.registration.ui.strings
import br.alexandregpereira.hunter.ui.compose.AppTextField
import br.alexandregpereira.hunter.ui.compose.form.FormLazy
import br.alexandregpereira.hunter.ui.compose.form.formItem

@Suppress("FunctionName")
internal fun LazyListScope.MonsterAbilityScoresForm(
    keys: Iterator<String>,
    abilityScores: List<AbilityScoreState>,
    onChanged: (List<AbilityScoreState>) -> Unit = {}
) = FormLazy(
    titleKey = keys.next(),
    title = { strings.abilityScores },
) {
    val newAbilityScores = abilityScores.toMutableList()
    abilityScores.fastForEachIndexed { i, abilityScore ->
        formItem(key = keys.next()) {
            AppTextField(
                value = abilityScore.value,
                label = abilityScore.name,
                onValueChange = { newValue ->
                    onChanged(newAbilityScores.changeAt(i) { copy(value = newValue) })
                }
            )
        }
    }
}
