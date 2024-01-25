package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.util.fastForEachIndexed
import br.alexandregpereira.hunter.domain.model.AbilityScore
import br.alexandregpereira.hunter.monster.registration.R
import br.alexandregpereira.hunter.monster.registration.ui.changeAt
import br.alexandregpereira.hunter.ui.compose.AppTextField

@Suppress("FunctionName")
internal fun LazyListScope.MonsterAbilityScoresForm(
    abilityScores: List<AbilityScore>,
    onChanged: (List<AbilityScore>) -> Unit = {}
) {
    val key = "abilityScores"
    FormLazy(
        key = key,
        title = { stringResource(R.string.monster_registration_ability_scores) },
    ) {
        val newAbilityScores = abilityScores.toMutableList()
        abilityScores.fastForEachIndexed { i, abilityScore ->
            formItem(key = "$key-$i") {
                AppTextField(
                    value = abilityScore.value,
                    label = abilityScore.type.toState().getStringResource(),
                    onValueChange = { newValue ->
                        onChanged(newAbilityScores.changeAt(i) { copy(value = newValue) })
                    }
                )
            }
        }
    }
}
