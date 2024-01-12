package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.util.fastForEachIndexed
import br.alexandregpereira.hunter.domain.model.AbilityScore
import br.alexandregpereira.hunter.monster.registration.R
import br.alexandregpereira.hunter.monster.registration.ui.changeAt
import br.alexandregpereira.hunter.ui.compose.AppTextField
import br.alexandregpereira.hunter.ui.compose.Form

@Composable
internal fun MonsterAbilityScoresForm(
    abilityScores: List<AbilityScore>,
    modifier: Modifier = Modifier,
    onChanged: (List<AbilityScore>) -> Unit = {}
) = Form(
    modifier = modifier,
    title = stringResource(R.string.monster_registration_ability_scores),
) {
    val newAbilityScores = abilityScores.toMutableList()
    abilityScores.fastForEachIndexed { i, abilityScore ->
        AppTextField(
            value = abilityScore.value,
            label = abilityScore.type.toState().getStringResource(),
            onValueChange = { newValue ->
                onChanged(newAbilityScores.changeAt(i) { copy(value = newValue) })
            }
        )
    }
}
