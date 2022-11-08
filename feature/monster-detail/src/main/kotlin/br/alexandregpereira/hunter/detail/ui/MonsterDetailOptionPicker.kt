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
) = BottomSheet(
    opened = showOptions,
    onClose = onClosed,
    swipeTriggerDistance = 80.dp
) {
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