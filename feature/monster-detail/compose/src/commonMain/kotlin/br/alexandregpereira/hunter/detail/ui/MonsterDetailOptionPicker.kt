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

package br.alexandregpereira.hunter.detail.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.monster.detail.MonsterDetailOptionState
import br.alexandregpereira.hunter.ui.compose.BottomSheet
import br.alexandregpereira.hunter.ui.compose.animatePressed
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun MonsterDetailOptionPicker(
    options: List<MonsterDetailOptionState>,
    showOptions: Boolean,
    contentPadding: PaddingValues = PaddingValues(),
    maxWidth: Dp = Dp.Unspecified,
    widthFraction: Float = .3f,
    onOptionSelected: (MonsterDetailOptionState) -> Unit = {},
    onClosed: () -> Unit = {}
) = BottomSheet(
    opened = showOptions,
    maxWidth = maxWidth,
    widthFraction = widthFraction,
    onClose = onClosed,
) {
    MonsterDetailOptions(options, contentPadding, onOptionSelected)
}

@Composable
private fun MonsterDetailOptions(
    options: List<MonsterDetailOptionState>,
    contentPadding: PaddingValues = PaddingValues(),
    onOptionSelected: (MonsterDetailOptionState) -> Unit = {},
) {
    Column {
        Text(
            text = strings.options,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(all = 16.dp)
        )

        options.forEach {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(all = 16.dp)
                    .animatePressed(
                        onClick = { onOptionSelected(it) }
                    )
            ) {
                Text(
                    text = it.name,
                    fontSize = 16.sp,
                )
            }
        }

        Spacer(
            modifier = Modifier
                .height(contentPadding.calculateBottomPadding() + 16.dp)
                .fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun MonsterDetailOptionPickerPreview() {
    MonsterDetailOptionPicker(
        listOf(
            MonsterDetailOptionState(name = "Change to feet"),
            MonsterDetailOptionState(name = "Change to meters"),
        ),
        showOptions = true
    )
}