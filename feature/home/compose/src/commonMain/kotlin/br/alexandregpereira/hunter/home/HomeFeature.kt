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

package br.alexandregpereira.hunter.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import br.alexandregpereira.hunter.home.ui.HomeCategoryType
import br.alexandregpereira.hunter.home.ui.HomeScreen
import org.koin.compose.koinInject

@Composable
fun HomeFeature(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    val stateHolder: HomeStateHolder = koinInject()
    val state by stateHolder.state.collectAsState()

    HomeScreen(
        state = state.viewState,
        strings = state.strings,
        modifier = modifier,
        contentPadding = contentPadding,
        onMenuClick = { stateHolder.onIntent(HomeIntent.OpenSettings) },
        onSearchClick = { stateHolder.onIntent(HomeIntent.OpenSearch) },
        onCategoryClick = { type ->
            when (type) {
                HomeCategoryType.CREATURES -> stateHolder.onIntent(HomeIntent.OpenMonsterCompendium)
                HomeCategoryType.SPELLS -> stateHolder.onIntent(HomeIntent.OpenSpellCompendium)
                HomeCategoryType.CONDITIONS -> Unit
            }
        },
        onFolderClick = { folderName ->
            stateHolder.onIntent(HomeIntent.OpenFolderDetail(folderName = folderName))
        },
        onSeeAllFoldersClick = { stateHolder.onIntent(HomeIntent.OpenFolderList) },
    )
}
