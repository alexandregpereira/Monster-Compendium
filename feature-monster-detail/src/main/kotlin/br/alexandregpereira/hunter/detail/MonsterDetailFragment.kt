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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import br.alexandregpereira.hunter.detail.ui.MonsterDetail
import br.alexandregpereira.hunter.detail.ui.MonsterDetailOptionPicker
import br.alexandregpereira.hunter.ui.compose.CircularLoading
import br.alexandregpereira.hunter.ui.compose.Window
import br.alexandregpereira.hunter.ui.util.createComposeView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MonsterDetailFragment : Fragment() {

    @Inject
    internal lateinit var monsterDetailViewModelFactory: MonsterDetailViewModelFactory

    private val viewModel: MonsterDetailViewModel by viewModels {
        MonsterDetailViewModelFactory.provideFactory(
            monsterDetailViewModelFactory,
            arguments?.getString("index") ?: ""
        )
    }

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

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun MonsterDetail(
    viewModel: MonsterDetailViewModel,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) = Window {
    val viewState by viewModel.stateLiveData.observeAsState(MonsterDetailViewState())

    CircularLoading(viewState.isLoading) {
        viewState.monsters.takeIf { it.isNotEmpty() }?.let {
            MonsterDetail(
                it,
                viewState.initialMonsterIndex,
                contentPadding,
                onMonsterChanged = { monster ->
                    viewModel.setMonsterIndex(monster.index)
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
