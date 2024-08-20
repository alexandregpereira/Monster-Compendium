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
