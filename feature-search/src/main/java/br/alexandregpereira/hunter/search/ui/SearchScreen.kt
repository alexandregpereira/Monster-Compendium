/*
 * Hunter - DnD 5th edition monster compendium application
 * Copyright (C) 2022 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.search.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.ui.theme.HunterTheme

@Composable
internal fun SearchScreen(
    state: SearchViewState,
    contentPaddingValues: PaddingValues = PaddingValues(),
    onSearchValueChange: (String) -> Unit = {}
) = HunterTheme {
    Surface {
        Column(Modifier) {
            Spacer(modifier = Modifier.height(contentPaddingValues.calculateTopPadding()))
            SearchBar(
                text = state.searchValue,
                onValueChange = onSearchValueChange,
                modifier = Modifier.padding(16.dp)
            )

            SearchGrid(monsters = state.monsters, contentPadding = contentPaddingValues)
            Spacer(modifier = Modifier.height(contentPaddingValues.calculateBottomPadding()))
        }
    }
}
