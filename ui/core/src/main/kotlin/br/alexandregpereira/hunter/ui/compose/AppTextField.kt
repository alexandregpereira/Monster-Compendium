/*
 * Copyright 2022 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.alexandregpereira.hunter.ui.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun AppTextField(
    text: String,
    modifier: Modifier = Modifier,
    label: String = "",
    keyboardType: AppKeyboardType = AppKeyboardType.TEXT,
    multiline: Boolean = false,
    onValueChange: (String) -> Unit = {}
) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = text,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = !multiline,
        shape = RoundedCornerShape(20),
        maxLines = if (multiline) 6 else 1,
        modifier = modifier.fillMaxWidth(),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = MaterialTheme.colors.onSurface,
            backgroundColor = MaterialTheme.colors.surface
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = when (keyboardType) {
                AppKeyboardType.TEXT -> KeyboardType.Text
                AppKeyboardType.NUMBER -> KeyboardType.Number
            }
        ),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
    )
}

@Composable
fun AppTextField(
    value: Int,
    modifier: Modifier = Modifier,
    label: String = "",
    onValueChange: (Int) -> Unit = {}
) {
    AppTextField(
        text = value.takeUnless { it == 0 }?.toString().orEmpty(),
        label = label,
        keyboardType = AppKeyboardType.NUMBER,
        modifier = modifier,
        onValueChange = { newValue ->
            onValueChange(newValue.toIntOrNull() ?: 0)
        }
    )
}

enum class AppKeyboardType {
    TEXT,
    NUMBER
}
