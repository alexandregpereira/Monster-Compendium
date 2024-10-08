/*
 * Copyright (C) 2024 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.ui.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentColor
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue

@Composable
fun AppTextField(
    text: String,
    modifier: Modifier = Modifier,
    label: String = "",
    keyboardType: AppKeyboardType = AppKeyboardType.TEXT,
    multiline: Boolean = false,
    capitalize: Boolean = true,
    enabled: Boolean = true,
    showClearIcon: Boolean = enabled,
    onValueChange: (String) -> Unit = {},
    trailingIcon: @Composable (() -> Unit)? = {
        AnimatedVisibility(
            visible = text.isNotEmpty() && showClearIcon,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            IconButton(
                onClick = { onValueChange("") },
            ) {
                Icon(
                    imageVector = Icons.Filled.Clear,
                    contentDescription = "Clear",
                    tint = LocalContentColor.current
                )
            }
        }
    },
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    val focusManager = LocalFocusManager.current
    val capitalization = if (capitalize) {
        KeyboardCapitalization.Sentences
    } else KeyboardCapitalization.None

    OutlinedTextField(
        value = text,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = !multiline,
        shape = RoundedCornerShape(20),
        maxLines = if (multiline) 6 else 1,
        modifier = modifier.fillMaxWidth(),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = LocalContentColor.current,
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = when (keyboardType) {
                AppKeyboardType.TEXT -> KeyboardType.Text
                AppKeyboardType.NUMBER -> KeyboardType.Number
                AppKeyboardType.DECIMAL -> KeyboardType.Decimal
            },
            capitalization = capitalization,
        ),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        trailingIcon = trailingIcon,
        leadingIcon = leadingIcon,
        enabled = enabled,
    )
}

@Composable
fun AppTextField(
    text: TextFieldValue,
    modifier: Modifier = Modifier,
    label: String = "",
    keyboardType: AppKeyboardType = AppKeyboardType.TEXT,
    multiline: Boolean = false,
    capitalize: Boolean = true,
    enabled: Boolean = true,
    showClearIcon: Boolean = enabled,
    onValueChange: (TextFieldValue) -> Unit = {},
    trailingIcon: @Composable (() -> Unit)? = {
        AnimatedVisibility(
            visible = text.text.isNotEmpty() && showClearIcon,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            IconButton(
                onClick = { onValueChange(TextFieldValue("")) },
            ) {
                Icon(
                    imageVector = Icons.Filled.Clear,
                    contentDescription = "Clear",
                    tint = LocalContentColor.current
                )
            }
        }
    },
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    val focusManager = LocalFocusManager.current
    val capitalization = if (capitalize) {
        KeyboardCapitalization.Sentences
    } else KeyboardCapitalization.None

    OutlinedTextField(
        value = text,
        onValueChange = { newValue ->
            onValueChange(newValue)
        },
        label = { Text(label) },
        singleLine = !multiline,
        shape = RoundedCornerShape(20),
        maxLines = if (multiline) 6 else 1,
        modifier = modifier.fillMaxWidth(),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = LocalContentColor.current,
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = when (keyboardType) {
                AppKeyboardType.TEXT -> KeyboardType.Text
                AppKeyboardType.NUMBER -> KeyboardType.Number
                AppKeyboardType.DECIMAL -> KeyboardType.Decimal
            },
            capitalization = capitalization,
        ),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        trailingIcon = trailingIcon,
        leadingIcon = leadingIcon,
        enabled = enabled,
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
    NUMBER,
    DECIMAL
}

@Composable
fun ClearFocusWhenScrolling(scrollableState: ScrollableState) {
    val focusManager = LocalFocusManager.current
    LaunchedEffect(scrollableState.isScrollInProgress) {
        if (scrollableState.isScrollInProgress) focusManager.clearFocus()
    }
}
