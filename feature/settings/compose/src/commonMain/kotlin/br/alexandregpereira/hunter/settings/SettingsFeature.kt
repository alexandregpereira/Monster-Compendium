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

package br.alexandregpereira.hunter.settings

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalUriHandler
import br.alexandregpereira.hunter.settings.ui.MenuScreen
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.koinInject

@Composable
fun SettingsFeature(
    versionName: String,
    contentPadding: PaddingValues,
) {
    val viewModel: SettingsStateHolder = koinInject()

    val uriHandler = LocalUriHandler.current
    LaunchedEffect(viewModel, uriHandler) {
        viewModel.action.collectLatest { action ->
            when (action) {
                is SettingsViewAction.GoToExternalUrl -> uriHandler.openUri(action.url)
            }
        }
    }

    MenuScreen(
        state = viewModel.state.collectAsState().value,
        versionName = versionName,
        contentPadding = contentPadding,
        viewIntent = viewModel,
    )
}
