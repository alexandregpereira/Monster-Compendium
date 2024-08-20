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

package br.alexandregpereira.hunter.folder.detail

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import br.alexandregpereira.hunter.domain.folder.model.MonsterPreviewFolder
import br.alexandregpereira.hunter.domain.folder.model.MonsterPreviewFolderImageContentScale
import br.alexandregpereira.hunter.folder.detail.di.FolderDetailStateRecoveryQualifier
import br.alexandregpereira.hunter.folder.detail.ui.FolderDetailScreen
import br.alexandregpereira.hunter.ui.compendium.monster.ColorState
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterCardState
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterImageState
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterTypeState
import br.alexandregpereira.hunter.ui.compose.AppImageContentScale
import br.alexandregpereira.hunter.ui.compose.StateRecoveryLaunchedEffect
import org.koin.compose.koinInject
import org.koin.core.qualifier.named

@Composable
fun FolderDetailFeature(
    contentPadding: PaddingValues = PaddingValues()
) {
    StateRecoveryLaunchedEffect(
        key = FolderDetailStateRecoveryQualifier,
        stateRecovery = koinInject(named(FolderDetailStateRecoveryQualifier))
    )
    val viewModel: FolderDetailStateHolder = koinInject()
    val state by viewModel.state.collectAsState()

    FolderDetailScreen(
        isOpen = state.isOpen,
        folderName = state.folderName,
        monsters = remember(state.monsters) { state.monsters.asState() },
        initialFirstVisibleItemIndex = remember { state.firstVisibleItemIndex },
        initialFirstVisibleItemScrollOffset = remember { state.firstVisibleItemScrollOffset },
        contentPadding = contentPadding,
        onItemCLick = viewModel::onItemClick,
        onItemLongCLick = viewModel::onItemLongClick,
        onClose = viewModel::onClose,
        onScrollChanges = viewModel::onScrollChanges,
    )
}

private fun List<MonsterPreviewFolder>.asState(): List<MonsterCardState> = map {
    it.run {
        MonsterCardState(
            index = index,
            name = name,
            imageState = MonsterImageState(
                url = imageUrl,
                type = MonsterTypeState.valueOf(type.name),
                backgroundColor = ColorState(
                    light = backgroundColorLight,
                    dark = backgroundColorDark
                ),
                challengeRating = challengeRating,
                isHorizontal = isHorizontalImage,
                contentDescription = name,
                contentScale = when (imageContentScale) {
                    MonsterPreviewFolderImageContentScale.Fit -> AppImageContentScale.Fit
                    MonsterPreviewFolderImageContentScale.Crop -> AppImageContentScale.Crop
                }
            )
        )
    }
}
