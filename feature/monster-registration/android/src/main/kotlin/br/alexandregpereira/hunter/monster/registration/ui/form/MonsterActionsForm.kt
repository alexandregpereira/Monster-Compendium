package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import br.alexandregpereira.hunter.domain.model.Action
import br.alexandregpereira.hunter.domain.model.DamageDice
import br.alexandregpereira.hunter.domain.model.DamageType
import br.alexandregpereira.hunter.monster.registration.R
import br.alexandregpereira.hunter.monster.registration.ui.changeAt
import br.alexandregpereira.hunter.ui.compose.AppTextField
import br.alexandregpereira.hunter.ui.compose.PickerField

@Suppress("FunctionName")
internal fun LazyListScope.MonsterActionsForm(
    key: String,
    title: @Composable () -> String,
    actions: List<Action>,
    onChanged: (List<Action>) -> Unit = {}
) = FormLazy(key, title) {
    val newActions = actions.toMutableList()
    val damageTypes = DamageType.entries.filter { it != DamageType.OTHER }

    FormItems(
        items = newActions,
        addText = { stringResource(R.string.monster_registration_add_action) },
        removeText = { stringResource(R.string.monster_registration_remove_action) },
        key = key,
        createNew = { Action.create() },
        onChanged = onChanged
    ) { actionIndex, action ->
        val abilityDescription = action.abilityDescription
        formItem(key = "$key-action-name-${action.id}") {
            AppTextField(
                text = abilityDescription.name,
                label = stringResource(R.string.monster_registration_name),
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

        formItem(key = "$key-action-description-${action.id}") {
            AppTextField(
                text = abilityDescription.description,
                label = stringResource(R.string.monster_registration_description),
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

        formItem(key = "$key-action-attackBonus-${action.id}") {
            AppTextField(
                value = action.attackBonus ?: 0,
                label = stringResource(R.string.monster_registration_attack_bonus),
                onValueChange = { newValue ->
                    onChanged(newActions.changeAt(actionIndex) { copy(attackBonus = newValue) })
                }
            )
        }

        val damageDiceKey = "$key-actions-damageDices-${action.id}"
        FormItems(
            items = action.damageDices.toMutableList(),
            addText = { stringResource(R.string.monster_registration_add_damage_dice) },
            removeText = { stringResource(R.string.monster_registration_remove_damage_dice) },
            key = damageDiceKey,
            createNew = { DamageDice.create() },
            onChanged = {
                onChanged(
                    newActions.changeAt(actionIndex) { copy(damageDices = it) }
                )
            }
        ) { index, damageDice ->
            formItem(key = "$damageDiceKey-damageDice-type-${damageDice.index}") {
                PickerField(
                    value = damageDice.damage.type.toTypeState().getStringName(),
                    label = stringResource(R.string.monster_registration_damage_type),
                    options = damageTypes.map { it.toTypeState().getStringName() },
                    onValueChange = { optionIndex ->
                        onChanged(
                            newActions.changeDamageDiceAt(actionIndex, index) {
                                copy(damage = damage.copy(type = damageTypes[optionIndex]))
                            }
                        )
                    }
                )
            }

            formItem(key = "$damageDiceKey-damageDice-dice-${damageDice.index}") {
                AppTextField(
                    text = damageDice.dice,
                    label = stringResource(R.string.monster_registration_damage_dice),
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

private fun MutableList<Action>.changeDamageDiceAt(
    actionIndex: Int,
    damageDiceIndex: Int,
    copy: DamageDice.() -> DamageDice
): List<Action> {
    return changeAt(actionIndex) {
        copy(damageDices = damageDices.toMutableList().changeAt(damageDiceIndex, copy))
    }
}
