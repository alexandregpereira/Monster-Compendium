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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun MonsterFolderGrid(
    folders: List<Pair<String, Int>>,
    modifier: Modifier = Modifier,
    onFolderSelected: (String) -> Unit = {},
) {
    if (folders.isNotEmpty()) {
        val columnsCount = if (folders.size > 4) 4 else folders.size
        val itemHeight = 48
        LazyHorizontalGrid(
            modifier = modifier.height((columnsCount * itemHeight).dp),
            rows = GridCells.Fixed(columnsCount),
            horizontalArrangement = Arrangement.spacedBy(56.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(folders) { (folder, monsterQuantity) ->
                Box(
                    modifier = Modifier
                        .height(itemHeight.dp)
                        .defaultMinSize(minWidth = 48.dp)
                        .clickable(onClick = { onFolderSelected(folder) })
                ) {
                    Text(
                        text = "$folder ($monsterQuantity)",
                        modifier = Modifier.align(Alignment.CenterStart)
                    )
                }
            }
        }
    }
}
