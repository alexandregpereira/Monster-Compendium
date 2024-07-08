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

import br.alexandregpereira.hunter.app.ui.resources.Res
import br.alexandregpereira.hunter.app.ui.resources.ic_book
import br.alexandregpereira.hunter.app.ui.resources.ic_folder
import br.alexandregpereira.hunter.app.ui.resources.ic_menu
import br.alexandregpereira.hunter.app.ui.resources.ic_search
import org.jetbrains.compose.resources.DrawableResource

internal data class MainViewState(
    val bottomBarItemSelectedIndex: Int = 0,
    val bottomBarItems: List<BottomBarItem> = emptyList(),
    internal val topContentStack: Set<String> = setOf(),
) {

    val showBottomBar: Boolean = topContentStack.isEmpty()
    val bottomBarItemSelected: BottomBarItem? = bottomBarItems.getOrNull(bottomBarItemSelectedIndex)
}

internal fun MainViewState.addTopContentStack(
    topContent: String,
): MainViewState {
    val topContentStack = topContentStack + topContent
    return copy(topContentStack = topContentStack)
}

internal fun MainViewState.removeTopContentStack(
    topContent: String,
): MainViewState {
    val topContentStack = topContentStack.toMutableSet().apply {
        remove(topContent)
    }.toSet()
    return copy(
        topContentStack = topContentStack,
    )
}

internal enum class BottomBarItemIcon(val value: DrawableResource) {
    COMPENDIUM(value = Res.drawable.ic_book),
    SEARCH(value = Res.drawable.ic_search),
    FOLDERS(value = Res.drawable.ic_folder),
    SETTINGS(value = Res.drawable.ic_menu)
}

internal data class BottomBarItem(
    val icon: BottomBarItemIcon = BottomBarItemIcon.COMPENDIUM,
    val text: String = "",
)
