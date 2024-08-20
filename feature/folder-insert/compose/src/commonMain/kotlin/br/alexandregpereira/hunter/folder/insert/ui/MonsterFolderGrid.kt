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
