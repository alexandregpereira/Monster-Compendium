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

package br.alexandregpereira.hunter.folder.list

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import br.alexandregpereira.hunter.folder.list.di.FolderListStateRecoveryQualifier
import br.alexandregpereira.hunter.folder.list.ui.FolderListScreen
import br.alexandregpereira.hunter.ui.compose.StateRecoveryLaunchedEffect
import org.koin.compose.koinInject
import org.koin.core.qualifier.named

@Composable
fun FolderListFeature(
    contentPadding: PaddingValues = PaddingValues()
) {
    StateRecoveryLaunchedEffect(
        key = FolderListStateRecoveryQualifier,
        stateRecovery = koinInject(named(FolderListStateRecoveryQualifier))
    )
    val viewModel: FolderListStateHolder = koinInject()
    FolderListScreen(
        state = viewModel.state.collectAsState().value,
        contentPadding = contentPadding,
        onCLick = viewModel::onItemClick,
        onLongCLick = viewModel::onItemSelect,
        onItemSelectionClose = viewModel::onItemSelectionClose,
        onItemSelectionDeleteClick = viewModel::onItemSelectionDeleteClick,
        onItemSelectionAddToPreviewClick = viewModel::onItemSelectionAddToPreviewClick,
        onScrollChanges = viewModel::onScrollChanges,
    )
}
