package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.SpeedType
import br.alexandregpereira.hunter.monster.registration.R
import br.alexandregpereira.hunter.monster.registration.ui.changeAt
import br.alexandregpereira.hunter.ui.compose.AppTextField
import br.alexandregpereira.hunter.ui.compose.Form
import br.alexandregpereira.hunter.ui.compose.PickerField

@Composable
internal fun MonsterSpeedValuesForm(
    monster: Monster,
    modifier: Modifier = Modifier,
    onMonsterChanged: (Monster) -> Unit = {}
) {
    val speedValues = monster.speed.values
    val types = speedValues.map { it.type }
    val options = SpeedType.entries.filterNot { types.contains(it) }
    val optionsStrings = options.map { it.toTypeState().getString() }
    val newSpeedValues = speedValues.toMutableList()

    Form(
        modifier = modifier,
        title = stringResource(R.string.monster_registration_speed),
    ) {
        monster.speed.values.mapIndexed { index, speedValue ->
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
