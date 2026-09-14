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

package br.alexandregpereira.hunter.monster.compendium.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.ui.compose.AppTopBar
import br.alexandregpereira.hunter.ui.compose.AppTopBarIconButton

@Composable
internal fun MonsterCompendiumTopBar(
    title: String,
    contentDescription: String,
    listState: LazyGridState,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onCloseClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
) {
    AppTopBar(
        title = title,
        listState = listState,
        onCloseClick = onCloseClick,
        modifier = modifier.padding(top = contentPadding.calculateTopPadding()),
        actions = {
            AppTopBarIconButton(
                imageVector = Icons.Filled.Search,
                contentDescription = contentDescription,
                onClick = onSearchClick,
            )
        },
    )
}
