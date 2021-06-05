/*
 * Copyright (c) 2021 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.alexandregpereira.hunter.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import br.alexandregpereira.hunter.detail.ui.MonsterDetail
import br.alexandregpereira.hunter.detail.ui.MonsterDetailOptionPicker
import br.alexandregpereira.hunter.ui.compose.CircularLoading
import br.alexandregpereira.hunter.ui.compose.Window
import br.alexandregpereira.hunter.ui.util.createComposeView
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MonsterDetailFragment : Fragment() {

    private val viewModel: MonsterDetailViewModel by viewModel {
        parametersOf(arguments?.getString("index") ?: "")
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
