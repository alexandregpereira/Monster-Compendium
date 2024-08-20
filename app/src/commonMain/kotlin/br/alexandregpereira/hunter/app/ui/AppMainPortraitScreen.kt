/*
 * Copyright (C) 2024 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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
import br.alexandregpereira.hunter.app.MainViewEvent
import br.alexandregpereira.hunter.app.MainViewEvent.BottomNavigationItemClick
import br.alexandregpereira.hunter.app.MainViewState
import br.alexandregpereira.hunter.detail.MonsterDetailBottomSheets
import br.alexandregpereira.hunter.detail.MonsterDetailFeature
import br.alexandregpereira.hunter.monster.lore.detail.MonsterLoreDetailFeature

@Composable
internal fun AppMainPortraitScreen(
    state: MainViewState,
    contentPadding: PaddingValues,
    onEvent: (MainViewEvent) -> Unit
) = AppMainScreen(contentPadding) {
    AppBottomNavigationTransition(
        bottomBarItemSelected = state.bottomBarItemSelected,
        contentPadding = contentPadding,
        bottomBarItemSelectedIndex = state.bottomBarItemSelectedIndex,
        bottomBarItems = state.bottomBarItems,
        onClick = { onEvent(BottomNavigationItemClick(item = it)) },
    )
    Box {
        MonsterDetailFeature(contentPadding = contentPadding)
        MonsterDetailBottomSheets()
    }
    MonsterLoreDetailFeature(contentPadding = contentPadding)
}
