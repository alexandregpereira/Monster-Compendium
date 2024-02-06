package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import br.alexandregpereira.hunter.domain.model.Skill
import br.alexandregpereira.hunter.monster.registration.R
import br.alexandregpereira.hunter.monster.registration.ui.changeAt
import br.alexandregpereira.hunter.ui.compose.AppTextField

@Suppress("FunctionName")
internal fun LazyListScope.MonsterProficiencyForm(
    title: @Composable () -> String,
    proficiencies: List<Skill>,
    onChanged: (List<Skill>) -> Unit = {}
) {
    val mutableProficiencies = proficiencies.toMutableList()
    val key = "skills"
    FormLazy(
        key = key,
        title = title,
    ) {
        FormItems(
            key = key,
            items = mutableProficiencies,
            createNew = { Skill.create() },
            onChanged = onChanged
        ) { i, proficiency ->
            formItem(key = "$key-name-${proficiency.index}") {
                AppTextField(
                    text = proficiency.name,
                    label = stringResource(R.string.monster_registration_name),
                    onValueChange = { newValue ->
                        onChanged(mutableProficiencies.changeAt(i) { copy(name = newValue) })
                    }
                )
            }

            formItem(key = "$key-modifier-${proficiency.index}") {
                AppTextField(
                    value = proficiency.modifier,
                    label = proficiency.name,
                    onValueChange = { newValue ->
                        onChanged(
                            mutableProficiencies.changeAt(i) {
                                copy(modifier = newValue)
                            }
                        )
                    }
                )
            }
        }
    }
}
