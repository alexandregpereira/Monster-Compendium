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

package br.alexandregpereira.hunter.app.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.app.MainViewEvent
import br.alexandregpereira.hunter.app.MainViewEvent.BottomNavigationItemClick
import br.alexandregpereira.hunter.app.MainViewState
import br.alexandregpereira.hunter.detail.MonsterDetailFeature
import br.alexandregpereira.hunter.folder.preview.FolderPreviewFeature
import br.alexandregpereira.hunter.spell.detail.SpellDetailFeature

@Composable
fun MainScreen(
    state: MainViewState,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onEvent: (MainViewEvent) -> Unit
) {
    Box {
        val bottomBarNavigationSize = 56.dp
        val contentPaddingWithBottomBar = PaddingValues(
            top = contentPadding.calculateTopPadding(),
            bottom = contentPadding.calculateBottomPadding() + bottomBarNavigationSize,
        )

        BottomNavigationTransition(
            bottomBarItemSelected = state.bottomBarItemSelected,
            contentPadding = contentPaddingWithBottomBar
        )

        MonsterDetailFeature(
            contentPadding = contentPadding,
        )

        SpellDetailFeature(
            contentPadding = contentPadding,
        )

        FolderPreviewFeature(
            contentPadding = contentPaddingWithBottomBar,
        )

        AppBottomNavigation(
            showBottomBar = state.showBottomBar,
            bottomBarItemSelected = state.bottomBarItemSelected,
            contentPadding = contentPadding,
            onClick = { onEvent(BottomNavigationItemClick(item = it)) }
        )
    }
}
