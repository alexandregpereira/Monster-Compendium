/*
 * Copyright 2022 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.alexandregpereira.hunter.app

import androidx.lifecycle.SavedStateHandle

data class MainViewState(
    val bottomBarItemSelected: BottomBarItem = BottomBarItem.COMPENDIUM,
    internal val topContentStack: List<String> = listOf(),
) {

    val showBottomBar: Boolean = topContentStack.isEmpty()
}

internal fun MainViewState.addTopContentStack(
    topContent: String,
): MainViewState {
    return copy(topContentStack = topContentStack + topContent)
}

internal fun MainViewState.removeTopContentStack(
    topContent: String,
): MainViewState {
    return copy(
        topContentStack = topContentStack.toMutableList().apply {
            remove(topContent)
        }.toList()
    )
}

enum class BottomBarItem(val iconRes: Int, val stringRes: Int) {
    COMPENDIUM(iconRes = R.drawable.ic_book, stringRes = R.string.compendium),
    SEARCH(iconRes = R.drawable.ic_search, stringRes = R.string.search),
    FOLDERS(iconRes = R.drawable.ic_folder, stringRes = R.string.folders),
    SETTINGS(iconRes = R.drawable.ic_menu, stringRes = R.string.menu)
}

internal fun SavedStateHandle.getState(): MainViewState {
    return MainViewState(
        bottomBarItemSelected = BottomBarItem.entries[this["bottomBarItemSelected"] ?: 0],
        topContentStack = this.get<Array<String>>("topContentStack")?.toMutableList()
            ?: mutableListOf(),
    )
}

internal fun MainViewState.saveState(
    savedStateHandle: SavedStateHandle
): MainViewState {
    savedStateHandle["bottomBarItemSelected"] = this.bottomBarItemSelected.ordinal
    savedStateHandle["topContentStack"] = this.topContentStack.toTypedArray()
    return this
}
