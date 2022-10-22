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

package br.alexandregpereira.hunter.app

import androidx.lifecycle.SavedStateHandle

data class MainViewState(
    val bottomBarItemSelected: BottomBarItem = BottomBarItem.COMPENDIUM,
    val showBottomBar: Boolean = true
)

enum class BottomBarItem(val iconRes: Int, val stringRes: Int) {
    COMPENDIUM(iconRes = R.drawable.ic_book, stringRes = R.string.compendium),
    SEARCH(iconRes = R.drawable.ic_search, stringRes = R.string.search),
    SETTINGS(iconRes = R.drawable.ic_settings, stringRes = R.string.settings)
}

internal fun SavedStateHandle.getState(): MainViewState {
    return MainViewState(
        bottomBarItemSelected = BottomBarItem.values()[this["bottomBarItemSelected"] ?: 0],
        showBottomBar = this["showBottomBar"] ?: true
    )
}

internal fun MainViewState.saveState(
    savedStateHandle: SavedStateHandle
): MainViewState {
    savedStateHandle["bottomBarItemSelected"] = this.bottomBarItemSelected.ordinal
    savedStateHandle["showBottomBar"] = this.showBottomBar
    return this
}
