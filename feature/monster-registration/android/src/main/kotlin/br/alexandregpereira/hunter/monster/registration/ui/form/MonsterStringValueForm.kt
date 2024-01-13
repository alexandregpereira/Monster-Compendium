package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.alexandregpereira.hunter.ui.compose.Form
import br.alexandregpereira.hunter.ui.compose.FormField

@Composable
internal fun MonsterStringValueForm(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    onChanged: (String) -> Unit = {}
) {
    Form(
        modifier = modifier,
        title = title,
        formFields = listOf(
            FormField.Text(
                key = title,
                label = title,
                value = value,
            )
        ),
        onFormChanged = { field ->
            onChanged(field.stringValue)
        },
    )
}
