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
