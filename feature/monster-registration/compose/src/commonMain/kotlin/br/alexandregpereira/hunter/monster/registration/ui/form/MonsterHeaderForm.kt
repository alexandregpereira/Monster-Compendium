package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.foundation.lazy.LazyListScope
import br.alexandregpereira.hunter.monster.registration.MonsterInfoState
import br.alexandregpereira.hunter.monster.registration.ui.strings
import br.alexandregpereira.hunter.ui.compose.AppTextField
import br.alexandregpereira.hunter.ui.compose.PickerField

@Suppress("FunctionName")
internal fun LazyListScope.MonsterHeaderForm(
    keys: Iterator<String>,
    infoState: MonsterInfoState,
    onMonsterChanged: (MonsterInfoState) -> Unit = {}
) {
    FormLazy(
        titleKey = keys.next(),
        title = { strings.edit }
    ) {
        formItem(key = keys.next()) {
            AppTextField(
                text = infoState.name,
                label = strings.name,
                onValueChange = { onMonsterChanged(infoState.copy(name = it)) }
            )
        }
        formItem(key = keys.next()) {
            AppTextField(
                text = infoState.subtitle,
                label = strings.subtitle,
                onValueChange = { onMonsterChanged(infoState.copy(subtitle = it)) }
            )
        }
        formItem(key = keys.next()) {
            AppTextField(
                text = infoState.group,
                label = strings.group,
                onValueChange = {
                    onMonsterChanged(infoState.copy(group = it.takeUnless { it.isBlank() }.orEmpty()))
                }
            )
        }
        formItem(key = keys.next()) {
            PickerField(
                value = infoState.type,
                label = strings.type,
                options = infoState.typeOptions,
                onValueChange = { i ->
                    onMonsterChanged(infoState.copy(typeIndex = i))
                }
            )
        }
    }
}
