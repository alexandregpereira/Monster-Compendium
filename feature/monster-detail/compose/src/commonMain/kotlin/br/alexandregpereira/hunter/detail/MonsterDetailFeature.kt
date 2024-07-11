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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.detail.ui.LocalStrings
import br.alexandregpereira.hunter.detail.ui.MonsterDetailOptionPicker
import br.alexandregpereira.hunter.detail.ui.MonsterDetailScreen
import br.alexandregpereira.hunter.detail.ui.strings
import br.alexandregpereira.hunter.monster.detail.MonsterDetailStateHolder
import br.alexandregpereira.hunter.monster.detail.di.MonsterDetailStateRecoveryQualifier
import br.alexandregpereira.hunter.ui.compose.BackHandler
import br.alexandregpereira.hunter.ui.compose.ConfirmationBottomSheet
import br.alexandregpereira.hunter.ui.compose.FormBottomSheet
import br.alexandregpereira.hunter.ui.compose.FormField
import br.alexandregpereira.hunter.ui.compose.LoadingScreen
import br.alexandregpereira.hunter.ui.compose.LocalScreenSize
import br.alexandregpereira.hunter.ui.compose.StateRecoveryLaunchedEffect
import br.alexandregpereira.hunter.ui.compose.SwipeVerticalToDismiss
import br.alexandregpereira.hunter.ui.compose.getPlatformScreenSizeInfo
import br.alexandregpereira.hunter.ui.theme.HunterTheme
import br.alexandregpereira.hunter.ui.theme.Shapes
import org.koin.compose.koinInject
import org.koin.core.qualifier.named

@Composable
fun MonsterDetailFeature(
    contentPadding: PaddingValues = PaddingValues(0.dp),
) = HunterTheme {
    StateRecoveryLaunchedEffect(
        key = MonsterDetailStateRecoveryQualifier,
        stateRecovery = koinInject(named(MonsterDetailStateRecoveryQualifier)),
    )

    val viewModel: MonsterDetailStateHolder = koinInject()
    val viewState by viewModel.state.collectAsState()

    BackHandler(enabled = viewState.showDetail) {
        viewModel.onClose()
    }

    SwipeVerticalToDismiss(visible = viewState.showDetail, onClose = viewModel::onClose) {
        LoadingScreen(
            isLoading = viewState.isLoading,
            showCircularLoading = false,
            modifier = Modifier
                .background(MaterialTheme.colors.surface)
                .clip(Shapes.large)
        ) {
            if (viewState.monsters.isEmpty()) return@LoadingScreen
            CompositionLocalProvider(
                LocalStrings provides viewState.strings,
                LocalScreenSize provides getPlatformScreenSizeInfo()
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
                )

                MonsterDetailOptionPicker(
                    options = viewState.options,
                    showOptions = viewState.showOptions,
                    onOptionSelected = viewModel::onOptionClicked,
                    onClosed = viewModel::onShowOptionsClosed
                )

                FormBottomSheet(
                    title = strings.clone,
                    formFields = listOf(
                        FormField.Text(
                            key = "monsterName",
                            label = strings.cloneMonsterName,
                            value = viewState.monsterCloneName,
                        )
                    ),
                    buttonText = strings.save,
                    opened = viewState.showCloneForm,
                    contentPadding = contentPadding,
                    buttonEnabled = viewState.monsterCloneName.isNotBlank(),
                    onFormChanged = { viewModel.onCloneFormChanged(it.stringValue) },
                    onClosed = viewModel::onCloneFormClosed,
                    onSaved = viewModel::onCloneFormSaved,
                )

                ConfirmationBottomSheet(
                    show = viewState.showDeleteConfirmation,
                    description = strings.deleteQuestion,
                    buttonText = strings.deleteConfirmation,
                    contentPadding = contentPadding,
                    onConfirmed = viewModel::onDeleteConfirmed,
                    onClosed = viewModel::onDeleteClosed
                )

                ConfirmationBottomSheet(
                    show = viewState.showResetConfirmation,
                    description = strings.resetQuestion,
                    buttonText = strings.resetConfirmation,
                    contentPadding = contentPadding,
                    onConfirmed = viewModel::onResetConfirmed,
                    onClosed = viewModel::onResetClosed
                )
            }
        }
    }
}
