package br.alexandregpereira.hunter.ui.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.ui.R

@Composable
fun FormBottomSheet(
    formFields: List<FormField>,
    opened: Boolean,
    title: String? = null,
    buttonText: String = stringResource(R.string.ui_core_save),
    buttonEnabled: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(),
    onFormChanged: (FormField) -> Unit = {},
    onClosed: () -> Unit = {},
    onSaved: () -> Unit = {},
) = BottomSheet(
    opened = opened,
    onClose = onClosed,
    contentPadding = contentPadding,
) {
    Form(
        title = title,
        formFields = formFields,
        modifier = Modifier
            .padding(top = 16.dp)
            .padding(horizontal = 16.dp),
        onFormChanged = onFormChanged,
    )

    Spacer(modifier = Modifier.padding(top = 16.dp))

    AppButton(
        text = buttonText,
        modifier = Modifier.padding(horizontal = 16.dp),
        enabled = buttonEnabled,
        onClick = onSaved
    )

    Spacer(modifier = Modifier.padding(top = 16.dp))
}

@Composable
fun Form(
    formFields: List<FormField>,
    modifier: Modifier = Modifier,
    title: String? = null,
    onFormChanged: (FormField) -> Unit = {},
) = Column(modifier) {

    if (!title.isNullOrBlank()) {
        ScreenHeader(
            title = title,
        )

        Spacer(modifier = Modifier.padding(top = 24.dp))
    }

    formFields.forEach { formField ->
        when (formField) {
            is FormField.Text -> AppTextField(
                text = formField.value,
                label = formField.label,
                onValueChange = { newValue -> onFormChanged(formField.copy(value = newValue)) }
            )
            is FormField.Number -> AppTextField(
                text = formField.value.takeUnless { it == 0 }?.toString().orEmpty(),
                label = formField.label,
                keyboardType = AppKeyboardType.NUMBER,
                onValueChange = { newValue ->
                    onFormChanged(formField.copy(value = newValue.toIntOrNull() ?: 0))
                }
            )
            is FormField.Picker -> PickerField(
                value = formField.value,
                label = formField.label,
                options = formField.options,
                onValueChange = { newValue ->
                    onFormChanged(formField.copy(value = newValue))
                }
            )
        }

        Spacer(modifier = Modifier.padding(vertical = 8.dp))
    }
}

sealed class FormField {

    abstract val key: String
    abstract val label: String

    val stringValue: String
        get() = when (this) {
            is Text -> value
            is Number -> value.toString()
            is Picker -> value
        }

    val intValue: Int
        get() = when (this) {
            is Text -> value.toIntOrNull() ?: 0
            is Number -> value
            is Picker -> value.toIntOrNull() ?: 0
        }

    data class Text(
        override val key: String,
        override val label: String,
        val value: String,
    ) : FormField()

    data class Number(
        override val key: String,
        override val label: String,
        val value: Int,
    ) : FormField()

    data class Picker(
        override val key: String,
        override val label: String,
        val value: String,
        val options: List<String>,
    ) : FormField()
}

val FormField.selectedIndex: Int
    get() = when (this) {
        is FormField.Picker -> options.indexOf(value)
        else -> -1
    }

@Preview
@Composable
fun FormBottomSheetPreview() = Window {
    FormBottomSheet(
        title = "Form",
        formFields = listOf(
            FormField.Text(
                key = "key1",
                label = "Label 1",
                value = "Value 1",
            ),
            FormField.Number(
                key = "key2",
                label = "Label 2",
                value = 2,
            ),
            FormField.Picker(
                key = "key3",
                label = "Label 3",
                value = "",
                options = listOf("Option 1", "Option 2", "Option 3"),
            ),
        ),
        opened = true,
    )
}

@Preview
@Composable
fun FormBottomSheetNoTitlePreview() = Window {
    FormBottomSheet(
        formFields = listOf(
            FormField.Text(
                key = "key1",
                label = "Label 1",
                value = "Value 1",
            ),
            FormField.Number(
                key = "key2",
                label = "Label 2",
                value = 2,
            ),
            FormField.Picker(
                key = "key3",
                label = "Label 3",
                value = "",
                options = listOf("Option 1", "Option 2", "Option 3"),
            ),
        ),
        opened = true,
    )
}
