package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import br.alexandregpereira.hunter.monster.registration.ActionState
import br.alexandregpereira.hunter.monster.registration.DamageDiceState
import br.alexandregpereira.hunter.monster.registration.ui.changeAt
import br.alexandregpereira.hunter.monster.registration.ui.strings
import br.alexandregpereira.hunter.ui.compose.AppTextField
import br.alexandregpereira.hunter.ui.compose.PickerField

@Suppress("FunctionName")
internal fun LazyListScope.MonsterActionsForm(
    keys: Iterator<String>,
    title: @Composable () -> String,
    actions: List<ActionState>,
    onChanged: (List<ActionState>) -> Unit = {}
) = FormLazy(titleKey = keys.next(), title) {
    val newActions = actions.toMutableList()

    FormItems(
        items = newActions,
        addText = { strings.addAction },
        removeText = { strings.removeAction },
        keys = keys,
        createNew = { ActionState() },
        onChanged = onChanged
    ) { actionIndex, action ->
        val abilityDescription = action.abilityDescription
        formItem(key = keys.next()) {
            AppTextField(
                text = abilityDescription.name,
                label = strings.name,
                onValueChange = { newValue ->
                    onChanged(
                        newActions.changeAt(actionIndex) {
                            copy(
                                abilityDescription = actions[actionIndex].abilityDescription.copy(
                                    name = newValue
                                )
                            )
                        }
                    )
                }
            )
        }

        formItem(key = keys.next()) {
            AppTextField(
                text = abilityDescription.description,
                label = strings.description,
                multiline = true,
                onValueChange = { newValue ->
                    onChanged(
                        newActions.changeAt(actionIndex) {
                            copy(
                                abilityDescription = actions[actionIndex].abilityDescription.copy(
                                    description = newValue
                                )
                            )
                        }
                    )
                }
            )
        }

        formItem(key = keys.next()) {
            AppTextField(
                value = action.attackBonus ?: 0,
                label = strings.attackBonus,
                onValueChange = { newValue ->
                    onChanged(newActions.changeAt(actionIndex) { copy(attackBonus = newValue) })
                }
            )
        }

        FormItems(
            items = action.damageDices.toMutableList(),
            addText = { strings.addDamageDice },
            removeText = { strings.removeDamageDice },
            keys = keys,
            createNew = { DamageDiceState() },
            onChanged = {
                onChanged(
                    newActions.changeAt(actionIndex) { copy(damageDices = it) }
                )
            }
        ) { index, damageDice ->
            formItem(key = keys.next()) {
                PickerField(
                    value = damageDice.name,
                    label = strings.damageType,
                    options = damageDice.damage.filteredOptions,
                    onValueChange = { optionIndex ->
                        onChanged(
                            newActions.changeDamageDiceAt(actionIndex, index) {
                                copy(damage = damage.copy(selectedIndex = damageDice.damage.selectedIndex(optionIndex)))
                            }
                        )
                    }
                )
            }

            formItem(key = keys.next()) {
                AppTextField(
                    text = damageDice.dice,
                    label = strings.damageDice,
                    onValueChange = { newValue ->
                        onChanged(
                            newActions.changeDamageDiceAt(actionIndex, index) {
                                copy(dice = newValue)
                            }
                        )
                    }
                )
            }
        }
    }
}

private fun MutableList<ActionState>.changeDamageDiceAt(
    actionIndex: Int,
    damageDiceIndex: Int,
    copy: DamageDiceState.() -> DamageDiceState
): List<ActionState> {
    return changeAt(actionIndex) {
        copy(damageDices = damageDices.toMutableList().changeAt(damageDiceIndex, copy))
    }
}
