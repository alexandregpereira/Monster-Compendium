/*
 * Copyright (C) 2022 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.spell.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import br.alexandregpereira.hunter.spell.detail.ui.SpellDetailScreen
import br.alexandregpereira.hunter.ui.util.createComposeView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SpellDetailFragment : Fragment() {

    private val viewModel: SpellDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return requireContext().createComposeView { contentPadding ->
            val state by viewModel.state.collectAsState()
            SpellDetailScreen(
                state = state,
                contentPadding = contentPadding,
                onClose = viewModel::onClose
            )
        }
    }
}
