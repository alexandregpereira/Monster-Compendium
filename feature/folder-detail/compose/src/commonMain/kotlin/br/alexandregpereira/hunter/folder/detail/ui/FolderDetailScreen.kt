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

package br.alexandregpereira.hunter.folder.detail.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import br.alexandregpereira.hunter.domain.folder.model.MonsterPreviewFolder
import br.alexandregpereira.hunter.folder.detail.FolderDetailState
import br.alexandregpereira.hunter.ui.compendium.CompendiumItemState.Item
import br.alexandregpereira.hunter.ui.compendium.CompendiumItemState.Title
import br.alexandregpereira.hunter.ui.compendium.monster.ColorState
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterCardState
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterCompendium
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterImageState
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterTypeState
import br.alexandregpereira.hunter.ui.compose.BackHandler
import br.alexandregpereira.hunter.ui.compose.SwipeVerticalToDismiss
import br.alexandregpereira.hunter.ui.compose.Window

@Composable
internal fun FolderDetailScreen(
    state: FolderDetailState,
    contentPadding: PaddingValues = PaddingValues(),
    onItemCLick: (index: String) -> Unit = {},
    onItemLongCLick: (index: String) -> Unit = {},
    onClose: () -> Unit = {}
) {
    BackHandler(enabled = state.isOpen, onBack = onClose)

    val monsters = remember(state.monsters) { state.monsters.asState() }
    SwipeVerticalToDismiss(visible = state.isOpen, onClose = onClose) {
        Window(Modifier.fillMaxSize()) {
            val items = listOf(
                Title(
                    value = state.folderName,
                    isHeader = true
                ),
            ) + monsters.map {
                Item(value = it)
            }
            MonsterCompendium(
                items = items,
                animateItems = true,
                contentPadding = contentPadding,
                onItemCLick = onItemCLick,
                onItemLongCLick = onItemLongCLick
            )
        }
    }
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
                contentDescription = name
            )
        )
    }
}
