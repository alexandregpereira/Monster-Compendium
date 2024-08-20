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

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ConfirmationBottomSheet(
    show: Boolean,
    description: String,
    buttonText: String,
    contentPadding: PaddingValues = PaddingValues(),
    maxWidth: Dp = Dp.Unspecified,
    widthFraction: Float = 1f,
    onConfirmed: () -> Unit = {},
    onClosed: () -> Unit = {}
) = BottomSheet(
    opened = show,
    contentPadding = PaddingValues(
        top = 16.dp + contentPadding.calculateTopPadding(),
        bottom = 16.dp + contentPadding.calculateBottomPadding(),
        start = 16.dp,
        end = 16.dp,
    ),
    maxWidth = maxWidth,
    widthFraction = widthFraction,
    onClose = onClosed,
) {
    Spacer(modifier = Modifier.height(16.dp))

    ScreenHeader(
        title = description,
    )

    Spacer(modifier = Modifier.height(32.dp))

    AppButton(
        text = buttonText,
        onClick = onConfirmed
    )
}
