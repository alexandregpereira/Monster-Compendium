package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import br.alexandregpereira.hunter.domain.model.Action
import br.alexandregpereira.hunter.domain.model.DamageDice
import br.alexandregpereira.hunter.domain.model.DamageType
import br.alexandregpereira.hunter.monster.registration.R
import br.alexandregpereira.hunter.monster.registration.ui.changeAt
import br.alexandregpereira.hunter.ui.compose.AppTextField
import br.alexandregpereira.hunter.ui.compose.Form
import br.alexandregpereira.hunter.ui.compose.PickerField

@Composable
internal fun MonsterActionsForm(
    title: String,
    actions: List<Action>,
    modifier: Modifier = Modifier,
    onChanged: (List<Action>) -> Unit = {}
) = Form(modifier, title) {
    val newActions = actions.toMutableList()
    val damageTypes = DamageType.entries.filter { it != DamageType.OTHER }
    val damageTypeOptions = damageTypes.map { it.toTypeState().getStringName() }

    FormItems(
        items = newActions,
        addText = stringResource(R.string.monster_registration_add_action),
        removeText = stringResource(R.string.monster_registration_remove_action),
        createNew = { Action.create() },
        onChanged = onChanged
    ) { actionIndex, action ->
        val abilityDescription = action.abilityDescription
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

        AppTextField(
            value = action.attackBonus ?: 0,
            label = stringResource(R.string.monster_registration_attack_bonus),
            onValueChange = { newValue ->
                onChanged(newActions.changeAt(actionIndex) { copy(attackBonus = newValue) })
            }
        )

        FormItems(
            items = action.damageDices.toMutableList(),
            addText = stringResource(R.string.monster_registration_add_damage_dice),
            removeText = stringResource(R.string.monster_registration_remove_damage_dice),
            createNew = { DamageDice.create() },
            onChanged = {
                onChanged(
                    newActions.changeAt(actionIndex) { copy(damageDices = it) }
                )
            }
        ) { index, damageDice ->
            PickerField(
                value = damageDice.damage.type.toTypeState().getStringName(),
                label = stringResource(R.string.monster_registration_damage_type),
                options = damageTypeOptions,
                onValueChange = { optionIndex ->
                    onChanged(
                        newActions.changeDamageDiceAt(actionIndex, index) {
                            copy(damage = damage.copy(type = damageTypes[optionIndex]))
                        }
                    )
                }
            )

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

private fun MutableList<Action>.changeDamageDiceAt(
    actionIndex: Int,
    damageDiceIndex: Int,
    copy: DamageDice.() -> DamageDice
): List<Action> {
    return changeAt(actionIndex) {
        copy(damageDices = damageDices.toMutableList().changeAt(damageDiceIndex, copy))
    }
}
