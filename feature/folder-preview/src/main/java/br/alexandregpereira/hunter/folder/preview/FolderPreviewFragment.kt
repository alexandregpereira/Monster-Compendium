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

package br.alexandregpereira.hunter.folder.preview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import br.alexandregpereira.hunter.folder.preview.FolderPreviewAction.NavigateToDetail
import br.alexandregpereira.hunter.folder.preview.ui.FolderPreviewScreen
import br.alexandregpereira.hunter.navigation.Navigator
import br.alexandregpereira.hunter.ui.util.createComposeView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FolderPreviewFragment : Fragment() {

    private val viewModel: FolderPreviewViewModel by viewModels()

    @Inject
    internal lateinit var navigator: Navigator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return requireContext().createComposeView(withBottomBar = true) { contentPadding ->
            FolderPreviewScreen(
                state = viewModel.state.collectAsState().value,
                contentPadding = contentPadding,
                onClick = viewModel::onItemClick,
                onLongClick = viewModel::onItemLongClick,
            )

            Action(viewModel, navigator)
        }
    }
}

@Composable
internal fun Action(
    viewModel: FolderPreviewViewModel,
    navigator: Navigator,
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.action.collect { action ->
            when (action) {
                is NavigateToDetail -> navigator.navigateToDetail(
                    action.monsterIndex,
                    folderName = action.folderName
                )
            }
        }
    }
}
