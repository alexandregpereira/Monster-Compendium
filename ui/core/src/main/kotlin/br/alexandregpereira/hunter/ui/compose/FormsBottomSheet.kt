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
        modifier = Modifier.padding(top = 16.dp).padding(horizontal = 16.dp),
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
        AppTextField(
            text = formField.value,
            label = formField.label,
            onValueChange = { newValue -> onFormChanged(formField.copy(value = newValue)) }
        )
        Spacer(modifier = Modifier.padding(vertical = 8.dp))
    }
}

data class FormField(
    val key: String,
    val label: String,
    val value: String,
)

@Preview
@Composable
fun FormBottomSheetPreview() = Window {
    FormBottomSheet(
        title = "Form",
        formFields = listOf(
            FormField(
                key = "key1",
                label = "Label 1",
                value = "Value 1",
            ),
            FormField(
                key = "key2",
                label = "Label 2",
                value = "Value 2",
            ),
            FormField(
                key = "key3",
                label = "Label 3",
                value = "Value 3",
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
            FormField(
                key = "key1",
                label = "Label 1",
                value = "Value 1",
            ),
            FormField(
                key = "key2",
                label = "Label 2",
                value = "Value 2",
            ),
            FormField(
                key = "key3",
                label = "Label 3",
                value = "Value 3",
            ),
        ),
        opened = true,
    )
}
