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
