package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.util.fastForEachIndexed
import br.alexandregpereira.hunter.monster.registration.AbilityScoreState
import br.alexandregpereira.hunter.monster.registration.ui.changeAt
import br.alexandregpereira.hunter.monster.registration.ui.strings
import br.alexandregpereira.hunter.ui.compose.AppTextField

@Suppress("FunctionName")
internal fun LazyListScope.MonsterAbilityScoresForm(
    abilityScores: List<AbilityScoreState>,
    onChanged: (List<AbilityScoreState>) -> Unit = {}
) {
    val key = "abilityScores"
    FormLazy(
        key = key,
        title = { strings.abilityScores },
    ) {
        val newAbilityScores = abilityScores.toMutableList()
        abilityScores.fastForEachIndexed { i, abilityScore ->
            formItem(key = "$key-$i") {
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
}
