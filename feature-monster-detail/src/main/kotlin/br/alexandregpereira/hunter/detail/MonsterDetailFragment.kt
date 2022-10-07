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

package br.alexandregpereira.hunter.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import br.alexandregpereira.hunter.detail.ui.MonsterDetailOptionPicker
import br.alexandregpereira.hunter.detail.ui.MonsterDetailScreen
import br.alexandregpereira.hunter.ui.compose.CircularLoading
import br.alexandregpereira.hunter.ui.compose.Window
import br.alexandregpereira.hunter.ui.util.createComposeView
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MonsterDetailFragment : Fragment() {

    private val viewModel: MonsterDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return requireContext().createComposeView {
            MonsterDetail(viewModel, contentPadding = it)
        }
    }
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class)
@Composable
internal fun MonsterDetail(
    viewModel: MonsterDetailViewModel,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) = Window {
    val viewState by viewModel.state.collectAsState()

    CircularLoading(viewState.isLoading) {
        viewState.monsters.takeIf { it.isNotEmpty() }?.let {
            MonsterDetailScreen(
                it,
                viewState.initialMonsterIndex,
                contentPadding,
                onMonsterChanged = { monster ->
                    viewModel.monsterIndex = monster.index
                },
                onOptionsClicked = viewModel::onShowOptionsClicked
            )

            MonsterDetailOptionPicker(
                options = viewState.options,
                showOptions = viewState.showOptions,
                onOptionSelected = viewModel::onOptionClicked,
                onClosed = viewModel::onShowOptionsClosed
            )
        }
    }
}
