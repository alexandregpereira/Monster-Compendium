/*
 * Hunter - DnD 5th edition monster compendium application
 * Copyright (C) 2021 Alexandre Gomes Pereira
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

package br.alexandregpereira.hunter.monster.compendium

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import br.alexandregpereira.hunter.domain.Navigator
import br.alexandregpereira.hunter.monster.compendium.ui.MonsterCompendium
import br.alexandregpereira.hunter.ui.compose.CircularLoading
import br.alexandregpereira.hunter.ui.theme.HunterTheme
import br.alexandregpereira.hunter.ui.util.createComposeView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MonsterCompendiumFragment : Fragment() {

    private val viewModel: MonsterCompendiumViewModel by viewModels()
    @Inject internal lateinit var navigator: Navigator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return requireContext().createComposeView { padding ->
            MonsterCompendium(
                viewModel = viewModel,
                navigator = navigator,
                contentPadding = padding
            )
        }
    }
}

@Composable
internal fun MonsterCompendium(
    viewModel: MonsterCompendiumViewModel,
    navigator: Navigator,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) = HunterTheme {
    val viewState by viewModel.state.collectAsState()

    CircularLoading(viewState.isLoading) {
        val listState = rememberLazyListState(initialFirstVisibleItemIndex = viewState.initialScrollItemPosition)
        MonsterCompendium(
            monstersBySection = viewState.monstersBySection,
            listState = listState,
            contentPadding = contentPadding,
        ) {
            viewModel.navigateToDetail(index = it)
        }
        OnFirstVisibleItemChange(viewModel, listState)
        Action(viewModel, navigator)
    }
}

@Composable
internal fun Action(
    viewModel: MonsterCompendiumViewModel,
    navigator: Navigator
) {
    val action = viewModel.action.collectAsState().value?.getContentIfNotHandled() ?: return
    when (action) {
        is MonsterCompendiumAction.NavigateToDetail -> navigator.navigateToDetail(action.index)
    }
}

@Composable
internal fun OnFirstVisibleItemChange(
    viewModel: MonsterCompendiumViewModel,
    listState: LazyListState,
) {
    viewModel.saveCompendiumScrollItemPosition(listState.firstVisibleItemIndex)
}