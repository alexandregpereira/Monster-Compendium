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
import br.alexandregpereira.hunter.ui.compose.PickerField

@Composable
internal fun MonsterActionsForm(
    title: String,
    actions: List<Action>,
    modifier: Modifier = Modifier,
    onChanged: (List<Action>) -> Unit = {}
) {
    val newActions = actions.toMutableList()
    val damageTypes = DamageType.entries.filter { it != DamageType.OTHER }
    val damageTypeOptions = damageTypes.map { it.toTypeState().getStringName() }
    MonsterAbilityDescriptionForm(
        title = title,
        abilityDescriptions = actions.map { it.abilityDescription },
        modifier = modifier,
        onChanged = {
            onChanged(
                actions.mapIndexed { index, action ->
                    action.copy(
                        abilityDescription = it[index],
                    )
                }
            )
        }
    ) { actionIndex ->
        val action = actions[actionIndex]
        action.attackBonus?.let {
            AppTextField(
                value = it,
                label = stringResource(R.string.monster_registration_attack_bonus),
                onValueChange = { newValue ->
                    onChanged(newActions.changeAt(actionIndex) { copy(attackBonus = newValue) })
                }
            )
        }

        action.damageDices.takeIf { it.isNotEmpty() }?.forEachIndexed { index, damageDice ->
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
