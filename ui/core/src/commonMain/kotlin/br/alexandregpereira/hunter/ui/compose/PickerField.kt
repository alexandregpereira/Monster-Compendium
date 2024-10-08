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

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun PickerField(
    label: String,
    options: List<String>,
    modifier: Modifier = Modifier,
    value: String = "",
    onValueChange: (Int) -> Unit = {}
) {
    val isOpen = remember { mutableStateOf(false) }
    Box(modifier) {
        Column {
            AppTextField(
                text = value,
                label = label,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowDown,
                        contentDescription = "",
                        tint = MaterialTheme.colors.onSurface
                    )
                }
            )
            DropdownMenu(
                expanded = isOpen.value,
                onDismissRequest = { isOpen.value = false },
            ) {
                options.forEachIndexed { index, option ->
                    DropdownMenuItem(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            isOpen.value = false
                            onValueChange(index)
                        }
                    ) {
                        Text(
                            option,
                            color = MaterialTheme.colors.onSurface,
                            modifier = Modifier
                            .wrapContentWidth()
                            .align(Alignment.CenterVertically)
                        )
                    }
                }
            }
        }
        Spacer(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Transparent)
                .clickable(
                    onClick = { isOpen.value = options.isNotEmpty() }
                )
        )
    }
}

@Preview
@Composable
private fun PickerFieldPreview() = Window {
    val text = remember { mutableStateOf("") }
    val options = remember { listOf("Option 1", "Option 2", "Option 3") }
    PickerField(
        label = "Label",
        value = text.value,
        options = options,
        modifier = Modifier.padding(16.dp),
        onValueChange = { text.value = options[it] }
    )
}
