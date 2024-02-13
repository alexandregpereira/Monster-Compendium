package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.foundation.lazy.LazyListScope
import br.alexandregpereira.hunter.monster.registration.MonsterInfoState
import br.alexandregpereira.hunter.monster.registration.ui.strings
import br.alexandregpereira.hunter.ui.compose.AppTextField
import br.alexandregpereira.hunter.ui.compose.PickerField

@Suppress("FunctionName")
internal fun LazyListScope.MonsterHeaderForm(
    infoState: MonsterInfoState,
    onMonsterChanged: (MonsterInfoState) -> Unit = {}
) {
    val key = "monsterHeader"
    FormLazy(
        key = key,
        title = { strings.edit }
    ) {
        formItem(key = "$key-name") {
            AppTextField(
                text = infoState.name,
                label = strings.name,
                onValueChange = { onMonsterChanged(infoState.copy(name = it)) }
            )
        }
        formItem(key = "$key-subtitle") {
            AppTextField(
                text = infoState.subtitle,
                label = strings.subtitle,
                onValueChange = { onMonsterChanged(infoState.copy(subtitle = it)) }
            )
        }
        formItem(key = "$key-group") {
            AppTextField(
                text = infoState.group,
                label = strings.group,
                onValueChange = {
                    onMonsterChanged(infoState.copy(group = it.takeUnless { it.isBlank() }.orEmpty()))
                }
            )
        }
        formItem(key = "$key-imageUrl") {
            AppTextField(
                text = infoState.imageUrl,
                label = strings.imageUrl,
                onValueChange = {
                    onMonsterChanged(infoState.copy(imageUrl = it))
                }
            )
        }
        formItem(key = "$key-imageBackgroundColor") {
            AppTextField(
                text = infoState.backgroundColorLight,
                label = strings.imageBackgroundColor,
                onValueChange = {
                    onMonsterChanged(
                        infoState.copy(backgroundColorLight = it)
                    )
                }
            )
        }
        formItem(key = "$key-type") {
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
