package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.monster.registration.DamageState
import br.alexandregpereira.hunter.monster.registration.ui.changeAt
import br.alexandregpereira.hunter.monster.registration.ui.strings
import br.alexandregpereira.hunter.ui.compose.AppTextField
import br.alexandregpereira.hunter.ui.compose.PickerField

@Suppress("FunctionName")
internal fun LazyListScope.MonsterDamagesForm(
    key: String,
    title: @Composable () -> String,
    damages: List<DamageState>,
    onChanged: (List<DamageState>) -> Unit = {}
) {
    val newDamages = damages.toMutableList()
    FormLazy(
        key = key,
        title = title,
    ) {
        FormItems(
            key = key,
            items = newDamages,
            createNew = { DamageState() },
            onChanged = onChanged
        ) { i, damage ->
            val otherName = damage.otherName
            formItem(key = "$key-name-${damage.key}") {
                if (i != 0 && otherName != null) Spacer(modifier = Modifier.height(8.dp))

                PickerField(
                    value = damage.typeName,
                    label = strings.damageType,
                    options = damage.filteredOptions,
                    onValueChange = { optionIndex ->
                        onChanged(
                            newDamages.changeAt(i) {
                                copy(selectedIndex = damage.selectedIndex(optionIndex))
                            }
                        )
                    }
                )
            }

            formItem(key = "$key-name-other-${damage.key}") {
                if (otherName != null) {
                    AppTextField(
                        text = otherName,
                        label = damage.typeName,
                        onValueChange = { newValue ->
                            onChanged(newDamages.changeAt(i) { copy(otherName = newValue) })
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}
