/*
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

package br.alexandregpereira.hunter.search

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import br.alexandregpereira.hunter.search.ui.SearchScreen

@Composable
fun SearchScreenFeature(
    contentPadding: PaddingValues = PaddingValues(),
) {
    val viewModel: SearchViewModel = viewModel()

    SearchScreen(
        state = viewModel.state.collectAsState().value,
        contentPaddingValues = contentPadding,
        onSearchValueChange = viewModel::onSearchValueChange,
        onCardClick = viewModel::onItemClick,
        onCardLongClick = viewModel::onItemLongClick,
    )
}
