package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import br.alexandregpereira.hunter.domain.model.Skill
import br.alexandregpereira.hunter.monster.registration.R
import br.alexandregpereira.hunter.monster.registration.ui.changeAt
import br.alexandregpereira.hunter.ui.compose.AppKeyboardType
import br.alexandregpereira.hunter.ui.compose.AppTextField
import br.alexandregpereira.hunter.ui.compose.Form

@Composable
internal fun MonsterProficiencyForm(
    title: String,
    proficiencies: List<Skill>,
    modifier: Modifier = Modifier,
    onChanged: (List<Skill>) -> Unit = {}
) {
    val mutableProficiencies = proficiencies.toMutableList()
    Form(
        modifier = modifier,
        title = title,
    ) {
        proficiencies.forEachIndexed { i, proficiency ->
            AppTextField(
                text = proficiency.name,
                label = stringResource(R.string.monster_registration_name),
                onValueChange = { newValue ->
                    onChanged(mutableProficiencies.changeAt(i) { copy(name = newValue) })
                }
            )

            AppTextField(
                text = proficiency.modifier.toString(),
                label = proficiency.name,
                keyboardType = AppKeyboardType.NUMBER,
                onValueChange = { newValue ->
                    onChanged(
                        mutableProficiencies.changeAt(i) {
                            copy(modifier = newValue.toIntOrNull() ?: 0)
                        }
                    )
                }
            )

            AddButton()
        }

        if (proficiencies.isEmpty()) {
            AddButton()
        }
    }
}
