package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.SpeedType
import br.alexandregpereira.hunter.domain.model.SpeedValue
import br.alexandregpereira.hunter.monster.registration.R
import br.alexandregpereira.hunter.monster.registration.ui.changeAt
import br.alexandregpereira.hunter.ui.compose.AppTextField
import br.alexandregpereira.hunter.ui.compose.PickerField

@Suppress("FunctionName")
internal fun LazyListScope.MonsterSpeedValuesForm(
    monster: Monster,
    onMonsterChanged: (Monster) -> Unit = {}
) {
    val speedValues = monster.speed.values
    val types = speedValues.map { it.type }
    val options = SpeedType.entries.filterNot { types.contains(it) }
    val newSpeedValues = speedValues.toMutableList()
    val key = "speed"

    FormLazy(
        key = key,
        title = { stringResource(R.string.monster_registration_speed) },
    ) {
        FormItems(
            key = key,
            items = newSpeedValues,
            createNew = { SpeedValue.create() },
            onChanged = {
                onMonsterChanged(
                    monster.copy(
                        speed = monster.speed.copy(
                            values = it
                        )
                    )
                )
            }
        ) { index, speedValue ->
            formItem(key = "$key-name-$index") {
                val optionsStrings = options.map { it.toTypeState().getString() }
                val name = speedValue.type.toTypeState().getString()
                PickerField(
                    value = name,
                    label = stringResource(R.string.monster_registration_speed_type),
                    options = optionsStrings,
                    onValueChange = { optionIndex ->
                        onMonsterChanged(
                            monster.copy(
                                speed = monster.speed.copy(
                                    values = newSpeedValues.changeAt(index) {
                                        copy(
                                            type = options[optionIndex],
                                        )
                                    }
                                )
                            )
                        )
                    }
                )
            }

            formItem(key = "$key-value-$index") {
                val name = speedValue.type.toTypeState().getString()
                AppTextField(
                    text = speedValue.valueFormatted,
                    label = name,
                    onValueChange = { newValue ->
                        onMonsterChanged(
                            monster.copy(
                                speed = monster.speed.copy(
                                    values = newSpeedValues.changeAt(index) {
                                        copy(valueFormatted = newValue)
                                    }
                                )
                            )
                        )
                    }
                )
            }
        }
    }
}

private enum class SpeedTypeState(val stringRes: Int) {
    BURROW(R.string.monster_registration_speed_type_burrow),
    CLIMB(R.string.monster_registration_speed_type_climb),
    FLY(R.string.monster_registration_speed_type_fly),
    WALK(R.string.monster_registration_speed_type_walk),
    SWIM(R.string.monster_registration_speed_type_swim),
}

private fun SpeedType.toTypeState() = SpeedTypeState.valueOf(name)

@Composable
private fun SpeedTypeState.getString() = stringResource(stringRes)
