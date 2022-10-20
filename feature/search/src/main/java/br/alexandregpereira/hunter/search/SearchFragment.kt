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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import br.alexandregpereira.hunter.navigation.Navigator
import br.alexandregpereira.hunter.search.ui.SearchScreen
import br.alexandregpereira.hunter.ui.util.createComposeView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val viewModel: SearchViewModel by viewModels()

    @Inject
    internal lateinit var navigator: Navigator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return requireContext().createComposeView(withBottomBar = true) { padding ->
            SearchScreen(
                state = viewModel.state.collectAsState().value,
                contentPaddingValues = padding,
                onSearchValueChange = viewModel::onSearchValueChange,
                onCardClick = viewModel::onItemClick,
                onCardLongClick = viewModel::onItemLongClick,
            )

            LaunchedEffect(key1 = Unit) {
                viewModel.action.collect { action ->
                    when (action) {
                        is SearchAction.NavigateToDetail -> navigator.navigateToDetail(
                            action.index,
                            disablePageScroll = true
                        )
                    }
                }
            }
        }
    }
}
