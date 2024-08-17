package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import br.alexandregpereira.hunter.monster.registration.SkillState
import br.alexandregpereira.hunter.monster.registration.ui.changeAt
import br.alexandregpereira.hunter.monster.registration.ui.strings
import br.alexandregpereira.hunter.ui.compose.AppTextField
import br.alexandregpereira.hunter.ui.compose.form.FormItems
import br.alexandregpereira.hunter.ui.compose.form.FormLazy
import br.alexandregpereira.hunter.ui.compose.form.formItem

@Suppress("FunctionName")
internal fun LazyListScope.MonsterProficiencyForm(
    keys: Iterator<String>,
    title: @Composable () -> String,
    proficiencies: List<SkillState>,
    onChanged: (List<SkillState>) -> Unit = {}
) {
    val mutableProficiencies = proficiencies.toMutableList()
    FormLazy(
        titleKey = keys.next(),
        title = title,
    ) {
        FormItems(
            keys = keys,
            items = mutableProficiencies,
            createNew = { SkillState() },
            addText = { strings.add },
            removeText = { strings.remove },
            onChanged = onChanged
        ) { i, proficiency ->
            formItem(key = keys.next()) {
                AppTextField(
                    text = proficiency.name,
                    label = strings.name,
                    onValueChange = { newValue ->
                        onChanged(mutableProficiencies.changeAt(i) { copy(name = newValue) })
                    }
                )
            }

            formItem(key = keys.next()) {
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
