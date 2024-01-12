package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.SpeedType
import br.alexandregpereira.hunter.monster.registration.R
import br.alexandregpereira.hunter.ui.compose.Form
import br.alexandregpereira.hunter.ui.compose.FormField

@Composable
internal fun MonsterSpeedValuesForm(
    monster: Monster,
    modifier: Modifier = Modifier,
    onMonsterChanged: (Monster) -> Unit = {}
) {
    val speedValues = monster.speed.values
    val types = speedValues.map { it.type }
    val options = SpeedType.entries.filterNot { types.contains(it) }.map { it.name }

    Form(
        modifier = modifier,
        title = stringResource(R.string.monster_registration_speed),
        formFields = monster.speed.values.map { speedValue ->
            listOf(
                FormField.Picker(
                    key = "${speedValue.type.name}-type",
                    label = stringResource(R.string.monster_registration_speed_type),
                    value = speedValue.type.name,
                    options = options,
                ),
                FormField.Text(
                    key = "${speedValue.type.name}-name",
                    label = speedValue.type.name,
                    value = speedValue.valueFormatted,
                )
            )
        }.reduceOrNull { acc, texts -> acc + texts } ?: emptyList(),
        onFormChanged = { field ->
            val newSpeedValues = speedValues.toMutableList()
            val index = newSpeedValues.indexOfFirst { field.key.startsWith(it.type.name) }
            if (index != -1) {
                when (field.key) {
                    "${newSpeedValues[index].type.name}-type" -> {
                        newSpeedValues[index] = newSpeedValues[index].copy(
                            type = SpeedType.valueOf(field.stringValue),
                        )
                    }
                    "${newSpeedValues[index].type.name}-name" -> {
                        newSpeedValues[index] = newSpeedValues[index].copy(valueFormatted = field.stringValue)
                    }
                }
                onMonsterChanged(monster.copy(speed = monster.speed.copy(values = newSpeedValues)))
            }
        },
    )
}