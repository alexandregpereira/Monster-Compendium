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

package br.alexandregpereira.hunter.app

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.app.di.AppStateRecoveryQualifier
import br.alexandregpereira.hunter.app.ui.AppMainLandscapeScreen
import br.alexandregpereira.hunter.app.ui.AppMainPortraitScreen
import br.alexandregpereira.hunter.ui.compose.AppWindow
import br.alexandregpereira.hunter.ui.compose.LocalScreenSize
import br.alexandregpereira.hunter.ui.compose.ScreenSizeType.LandscapeCompact
import br.alexandregpereira.hunter.ui.compose.ScreenSizeType.LandscapeExpanded
import br.alexandregpereira.hunter.ui.compose.ScreenSizeType.Portrait
import br.alexandregpereira.hunter.ui.compose.StateRecoveryLaunchedEffect
import br.alexandregpereira.hunter.ui.compose.getPlatformScreenSizeInfo
import org.koin.compose.KoinContext
import org.koin.compose.koinInject
import org.koin.core.qualifier.named

@Composable
internal fun HunterApp(
    contentPadding: PaddingValues = PaddingValues(0.dp),
) = AppWindow {
    KoinContext {
        StateRecoveryLaunchedEffect(
            key = AppStateRecoveryQualifier,
            stateRecovery = koinInject(named(AppStateRecoveryQualifier)),
        )

        val viewModel: MainViewModel = koinInject()
        val state by viewModel.state.collectAsState()
        CompositionLocalProvider(
            LocalScreenSize provides getPlatformScreenSizeInfo()
        ) {
            val screenSize = LocalScreenSize.current

            when (screenSize.type) {
                Portrait -> AppMainPortraitScreen(
                    state = state,
                    contentPadding = contentPadding,
                    onEvent = viewModel::onEvent
                )
                LandscapeCompact,
                LandscapeExpanded -> {
                    val leftPanelFraction = if (screenSize.type == LandscapeExpanded) {
                        0.7f
                    } else 0.5f
                    AppMainLandscapeScreen(
                        state = state,
                        contentPadding = contentPadding,
                        leftPanelFraction = leftPanelFraction,
                        onEvent = viewModel::onEvent
                    )
                }
            }
        }
    }
}
