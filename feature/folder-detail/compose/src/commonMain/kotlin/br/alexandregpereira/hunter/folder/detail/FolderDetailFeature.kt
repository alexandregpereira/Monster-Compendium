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
