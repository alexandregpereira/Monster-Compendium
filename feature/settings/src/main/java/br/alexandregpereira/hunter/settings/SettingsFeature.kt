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

package br.alexandregpereira.hunter.settings

import android.app.Activity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import br.alexandregpereira.hunter.settings.ui.SettingsScreen

@Composable
fun SettingsFeature(
    versionName: String,
    contentPadding: PaddingValues,
) {
    val viewModel: SettingsViewModel = viewModel()
    val context = LocalContext.current
    SettingsScreen(
        state = viewModel.state.collectAsState().value,
        versionName = versionName,
        contentPadding = contentPadding,
        onImageBaseUrlChange = viewModel::onImageBaseUrlChange,
        onAlternativeSourceBaseUrlChange = viewModel::onAlternativeSourceBaseUrlChange,
        onSaveButtonClick = viewModel::onSaveButtonClick,
    )

    LaunchedEffect(Unit) {
        viewModel.action.collect { action ->
            when (action) {
                SettingsAction.CloseApp -> (context as Activity).finish()
            }
        }
    }
}
