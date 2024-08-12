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

package br.alexandregpereira.hunter.detail

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.detail.ui.LocalStrings
import br.alexandregpereira.hunter.detail.ui.MonsterDetailScreen
import br.alexandregpereira.hunter.monster.detail.MonsterDetailStateHolder
import br.alexandregpereira.hunter.monster.detail.di.MonsterDetailStateRecoveryQualifier
import br.alexandregpereira.hunter.ui.compose.AppScreen
import br.alexandregpereira.hunter.ui.compose.LoadingScreen
import br.alexandregpereira.hunter.ui.compose.StateRecoveryLaunchedEffect
import org.koin.compose.koinInject
import org.koin.core.qualifier.named

@Composable
fun MonsterDetailFeature(
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    StateRecoveryLaunchedEffect(
        key = MonsterDetailStateRecoveryQualifier,
        stateRecovery = koinInject(named(MonsterDetailStateRecoveryQualifier)),
    )

    val viewModel: MonsterDetailStateHolder = koinInject()
    val viewState by viewModel.state.collectAsState()

    AppScreen(
        isOpen = viewState.showDetail,
        contentPaddingValues = contentPadding,
        showCloseButton = false,
        backgroundColor = MaterialTheme.colors.background,
        onClose = viewModel::onClose
    ) {
        LoadingScreen(
            isLoading = viewState.isLoading,
            showCircularLoading = false,
        ) {
            if (viewState.monsters.isEmpty()) return@LoadingScreen
            CompositionLocalProvider(
                LocalStrings provides viewState.strings,
            ) {
                MonsterDetailScreen(
                    viewState.monsters,
                    viewState.initialMonsterListPositionIndex,
                    contentPadding,
                    onMonsterChanged = { monster ->
                        viewModel.onMonsterChanged(monster.index)
                    },
                    onOptionsClicked = viewModel::onShowOptionsClicked,
                    onSpellClicked = viewModel::onSpellClicked,
                    onLoreClicked = viewModel::onLoreClicked,
                    onClose = viewModel::onClose,
                )
            }
        }
    }
}
