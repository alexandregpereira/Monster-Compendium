package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.ui.compose.Form
import br.alexandregpereira.hunter.ui.compose.FormField

@Composable
internal fun MonsterSpeedValuesForm(
    monster: Monster,
    modifier: Modifier = Modifier,
    onMonsterChanged: (Monster) -> Unit = {}
) {
    Form(
        modifier = modifier,
        title = "Speed",
        formFields = monster.speed.values.mapIndexed { index, speedValue ->
            FormField(
                key = "speedValue$index",
                label = speedValue.type.name,
                value = speedValue.valueFormatted,
            )
        },
        onFormChanged = { field ->
            val speedValues = monster.speed.values.toMutableList()
            val index = speedValues.indexOfFirst { it.type.name == field.key }
            if (index != -1) {
                speedValues[index] = speedValues[index].copy(valueFormatted = field.value)
                onMonsterChanged(monster.copy(speed = monster.speed.copy(values = speedValues)))
            }
        },
    )
}