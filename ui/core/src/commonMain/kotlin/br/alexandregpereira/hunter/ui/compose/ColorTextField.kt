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
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.ui.util.toColor

@Composable
fun ColorTextField(
    text: String,
    modifier: Modifier = Modifier,
    label: String = "",
    enabled: Boolean = true,
    onValueChange: (String) -> Unit = {},
)  = AppTextField(
    text = text,
    modifier = modifier,
    label = label,
    enabled = enabled,
    onValueChange = onValueChange,
    leadingIcon = {
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(text.toColor(), shape = CircleShape)
                .border(1.dp, MaterialTheme.colors.background, shape = CircleShape)
        )
    }
)
