package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import br.alexandregpereira.hunter.domain.model.Action
import br.alexandregpereira.hunter.domain.model.DamageType
import br.alexandregpereira.hunter.monster.registration.R
import br.alexandregpereira.hunter.ui.compose.FormField

@Composable
internal fun MonsterActionsForm(
    title: String,
    actions: List<Action>,
    modifier: Modifier = Modifier,
    onChanged: (List<Action>) -> Unit = {}
) {
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
        },
        getAdditionalFields = { index ->
            val action = actions[index]
            val damageDiceFields = action.damageDices.takeIf { it.isNotEmpty() }?.let { damageDices ->
                damageDices.map { damageDice ->
                    listOf(
                        FormField.Picker(
                            key = "damage_type-${action.id}",
                            label = "Damage Type",
                            value = damageDice.damage.type.name,
                            options = DamageType.entries.filter { it != DamageType.OTHER }
                                .map { it.name },
                        ),
                        FormField.Text(
                            key = "damage_dice-${action.id}",
                            label = "Damage Dice",
                            value = damageDice.dice,
                        ),
                    )
                }.reduceOrNull { acc, texts -> acc + texts }
            } ?: emptyList()

            listOfNotNull(
                action.attackBonus?.let {
                    FormField.Number(
                        key = "attack_bonus",
                        label = "Attack Bonus",
                        value = it,
                    )
                },
            ) + damageDiceFields
        },
        onAdditionalFieldChanged = { index, field ->
            val currentFormAction = actions[index]
            when (field.key) {
                "attack_bonus" -> {
                    onChanged(
                        actions.map {
                            if (it.id == currentFormAction.id) {
                                it.copy(attackBonus = field.intValue)
                            } else {
                                it
                            }
                        }
                    )
                }

                "damage_dice-${currentFormAction.id}" -> {
                    onChanged(
                        actions.map {
                            it.copy(
                                damageDices = it.damageDices.map { damageDice ->
                                    if (it.id == currentFormAction.id) {
                                        damageDice.copy(dice = field.stringValue)
                                    } else {
                                        damageDice
                                    }
                                }
                            )
                        }
                    )
                }

                "damage_type-${currentFormAction.id}" -> {
                    onChanged(
                        actions.map { action ->
                            action.copy(
                                damageDices = action.damageDices.map { damageDice ->
                                    damageDice.copy(
                                        damage = damageDice.damage.copy(
                                            type = if (action.id == currentFormAction.id) {
                                                DamageType.entries.first { it.name == field.stringValue }
                                            } else {
                                                damageDice.damage.type
                                            },
                                            name = if (action.id == currentFormAction.id) {
                                                field.stringValue
                                            } else {
                                                damageDice.damage.name
                                            }
                                        )
                                    )
                                }
                            )
                        }
                    )
                }
            }
        }
    )
}
