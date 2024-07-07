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
    val bottomBarItemSelectedIndex: Int = 0,
    val bottomBarItems: List<BottomBarItem> = emptyList(),
    internal val topContentStack: Set<String> = setOf(),
    val showBottomBar: Boolean = false,
) {

    val bottomBarItemSelected: BottomBarItem? = bottomBarItems.getOrNull(bottomBarItemSelectedIndex)
}

internal fun MainViewState.addTopContentStack(
    topContent: String,
): MainViewState {
    val topContentStack = topContentStack + topContent
    return copy(topContentStack = topContentStack, showBottomBar = topContentStack.isEmpty())
}

internal fun MainViewState.removeTopContentStack(
    topContent: String,
): MainViewState {
    val topContentStack = topContentStack.toMutableSet().apply {
        remove(topContent)
    }.toSet()
    return copy(
        topContentStack = topContentStack,
        showBottomBar = topContentStack.isEmpty(),
    )
}

enum class BottomBarItemIcon(val iconRes: Int) {
    COMPENDIUM(iconRes = R.drawable.ic_book),
    SEARCH(iconRes = R.drawable.ic_search),
    FOLDERS(iconRes = R.drawable.ic_folder),
    SETTINGS(iconRes = R.drawable.ic_menu)
}

data class BottomBarItem(
    val icon: BottomBarItemIcon = BottomBarItemIcon.COMPENDIUM,
    val text: String = "",
)

internal fun SavedStateHandle.getState(): MainViewState {
    return MainViewState(
        bottomBarItemSelectedIndex = this["bottomBarItemSelectedIndex"] ?: 0,
        topContentStack = this.get<Array<String>>("topContentStack")?.toSet()
            ?: setOf(),
    )
}

internal fun MainViewState.saveState(
    savedStateHandle: SavedStateHandle
): MainViewState {
    savedStateHandle["bottomBarItemSelectedIndex"] = this.bottomBarItemSelectedIndex
    savedStateHandle["topContentStack"] = this.topContentStack.toTypedArray()
    return this
}
