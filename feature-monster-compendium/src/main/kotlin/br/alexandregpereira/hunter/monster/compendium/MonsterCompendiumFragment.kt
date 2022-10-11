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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import br.alexandregpereira.hunter.monster.compendium.ui.MonsterCompendiumScreen
import br.alexandregpereira.hunter.navigation.Navigator
import br.alexandregpereira.hunter.ui.util.createComposeView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MonsterCompendiumFragment : Fragment() {

    private val viewModel: MonsterCompendiumViewModel by viewModels()

    @Inject
    internal lateinit var navigator: Navigator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return requireContext().createComposeView(withBottomBar = true) { contentPadding ->
            var compendiumIndex by remember { mutableStateOf(-1) }
            MonsterCompendiumScreen(
                state = viewModel.state.collectAsState().value,
                contentPadding = contentPadding,
                compendiumIndex = compendiumIndex,
                events = viewModel
            )
            Action(viewModel, navigator) { index ->
                compendiumIndex = index
            }
        }
    }
}

@Composable
internal fun Action(
    viewModel: MonsterCompendiumViewModel,
    navigator: Navigator,
    onNavigateToCompendiumIndex: (index: Int) -> Unit,
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.action.collect { action ->
            when (action) {
                is MonsterCompendiumAction.NavigateToDetail -> navigator.navigateToDetail(action.index)
                is MonsterCompendiumAction.NavigateToCompendiumIndex -> {
                    onNavigateToCompendiumIndex(action.index)
                }
            }
        }
    }
}
