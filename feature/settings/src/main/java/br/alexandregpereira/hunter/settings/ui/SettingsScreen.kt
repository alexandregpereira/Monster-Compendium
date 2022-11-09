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

package br.alexandregpereira.hunter.settings.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.settings.SettingsViewState
import br.alexandregpereira.hunter.ui.compose.Window

@Composable
internal fun SettingsScreen(
    state: SettingsViewState,
    versionName: String,
    contentPadding: PaddingValues = PaddingValues(),
    onImageBaseUrlChange: (String) -> Unit = {},
    onAlternativeSourceBaseUrlChange: (String) -> Unit = {},
    onSaveButtonClick: () -> Unit = {}
) = Window {
    Box(modifier = Modifier.fillMaxSize()) {
        Settings(
            imageBaseUrl = state.imageBaseUrl,
            alternativeSourceBaseUrl = state.alternativeSourceBaseUrl,
            saveButtonEnabled = state.saveButtonEnabled,
            onImageBaseUrlChange = onImageBaseUrlChange,
            onAlternativeSourceBaseUrlChange = onAlternativeSourceBaseUrlChange,
            onSaveButtonClick = onSaveButtonClick,
            modifier = Modifier.padding(
                top = contentPadding.calculateTopPadding(),
                bottom = contentPadding.calculateBottomPadding()
            )
        )

        Text(
            text = "v$versionName",
            fontWeight = FontWeight.Light,
            fontSize = 12.sp,
            modifier = Modifier.align(Alignment.BottomEnd)
                .padding(contentPadding)
                .padding(8.dp)
        )
    }
}

@Preview
@Composable
private fun SettingsScreenPreview() {
    SettingsScreen(
        state = SettingsViewState(),
        versionName = "1.20.1"
    )
}

