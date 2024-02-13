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

package br.alexandregpereira.hunter.folder.insert.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.folder.insert.MonsterPreviewState
import br.alexandregpereira.hunter.ui.compose.CircleImage

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun MonsterPreviewRow(
    monsters: List<MonsterPreviewState>,
    modifier: Modifier = Modifier,
    onLongClick: (String) -> Unit = {},
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(monsters, key = { it.index }) { state ->
            CircleImage(
                imageUrl = state.imageUrl,
                backgroundColor = if (isSystemInDarkTheme()) {
                    state.backgroundColorDark
                } else {
                    state.backgroundColorLight
                },
                contentDescription = state.name,
                modifier = Modifier.animateItemPlacement(),
                onLongClick = { onLongClick(state.index) }
            )
        }
    }
}
