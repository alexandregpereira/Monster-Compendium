package br.alexandregpereira.hunter.ui.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager

@Composable
fun AppTextField(
    text: String,
    modifier: Modifier = Modifier,
    label: String = "",
    onValueChange: (String) -> Unit = {}
) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = text,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        shape = CircleShape,
        modifier = modifier.fillMaxWidth(),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = MaterialTheme.colors.onSurface,
            backgroundColor = MaterialTheme.colors.surface
        ),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
    )
}
