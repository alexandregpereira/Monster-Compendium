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
                modifier = Modifier.animateItem(),
                onLongClick = { onLongClick(state.index) }
            )
        }
    }
}
