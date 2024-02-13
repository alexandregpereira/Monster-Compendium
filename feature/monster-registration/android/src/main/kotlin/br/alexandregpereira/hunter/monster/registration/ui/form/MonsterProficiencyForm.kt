package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import br.alexandregpereira.hunter.monster.registration.SkillState
import br.alexandregpereira.hunter.monster.registration.ui.changeAt
import br.alexandregpereira.hunter.monster.registration.ui.strings
import br.alexandregpereira.hunter.ui.compose.AppTextField

@Suppress("FunctionName")
internal fun LazyListScope.MonsterProficiencyForm(
    title: @Composable () -> String,
    proficiencies: List<SkillState>,
    onChanged: (List<SkillState>) -> Unit = {}
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
            createNew = { SkillState() },
            onChanged = onChanged
        ) { i, proficiency ->
            formItem(key = "$key-name-${proficiency.key}") {
                AppTextField(
                    text = proficiency.name,
                    label = strings.name,
                    onValueChange = { newValue ->
                        onChanged(mutableProficiencies.changeAt(i) { copy(name = newValue) })
                    }
                )
            }

            formItem(key = "$key-modifier-${proficiency.key}") {
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
