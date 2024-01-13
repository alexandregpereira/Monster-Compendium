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

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.ui.compose.AppButton
import br.alexandregpereira.hunter.ui.compose.BottomSheet
import br.alexandregpereira.hunter.ui.compose.ScreenHeader

@Composable
fun MonsterDeleteConfirmation(
    show: Boolean,
    contentPadding: PaddingValues = PaddingValues(),
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
    onClose = onClosed,
) {
    Spacer(modifier = Modifier.height(16.dp))

    ScreenHeader(
        title = "Are you sure you want to delete this monster?",
    )

    Spacer(modifier = Modifier.height(32.dp))

    AppButton(
        text = "I'm sure",
        onClick = onConfirmed
    )
}

@Preview
@Composable
private fun MonsterDeleteConfirmationPreview() {
    MonsterDeleteConfirmation(
        show = true,
        onConfirmed = {},
        onClosed = {}
    )
}