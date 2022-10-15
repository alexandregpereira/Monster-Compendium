/*
 * Hunter - DnD 5th edition monster compendium application
 * Copyright (C) 2021 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.detail.MonsterDetailOptionState
import br.alexandregpereira.hunter.detail.R
import br.alexandregpereira.hunter.ui.compose.BottomSheet
import br.alexandregpereira.hunter.ui.compose.animatePressed

@Composable
fun MonsterDetailOptionPicker(
    options: List<MonsterDetailOptionState>,
    showOptions: Boolean,
    onOptionSelected: (MonsterDetailOptionState) -> Unit = {},
    onClosed: () -> Unit = {}
) = BottomSheet(opened = showOptions, onClose = onClosed) {
    MonsterDetailOptions(options, onOptionSelected)
}

@Composable
private fun MonsterDetailOptions(
    options: List<MonsterDetailOptionState>,
    onOptionSelected: (MonsterDetailOptionState) -> Unit = {},
) {
    Column {
        Text(
            text = stringResource(R.string.monster_detail_options),
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
                .height(PaddingValues(all = 32.dp).calculateBottomPadding())
                .fillMaxWidth()
        )
    }
}

@Preview
@Composable
fun MonsterDetailOptionPickerPreview() {
    MonsterDetailOptionPicker(
        listOf(
            MonsterDetailOptionState.CHANGE_TO_FEET,
            MonsterDetailOptionState.CHANGE_TO_METERS
        ),
        showOptions = true
    )
}