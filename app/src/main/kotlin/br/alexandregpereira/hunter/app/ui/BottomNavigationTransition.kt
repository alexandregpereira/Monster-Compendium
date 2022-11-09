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

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import br.alexandregpereira.hunter.app.BottomBarItem
import br.alexandregpereira.hunter.app.BuildConfig
import br.alexandregpereira.hunter.folder.list.FolderListFeature
import br.alexandregpereira.hunter.monster.compendium.MonsterCompendiumFeature
import br.alexandregpereira.hunter.search.SearchScreenFeature
import br.alexandregpereira.hunter.settings.SettingsFeature

@Composable
fun BottomNavigationTransition(
    bottomBarItemSelected: BottomBarItem,
    contentPadding: PaddingValues = PaddingValues()
) {
    Crossfade(targetState = bottomBarItemSelected) { index ->
        when (index) {
            BottomBarItem.COMPENDIUM -> MonsterCompendiumFeature(
                contentPadding = contentPadding,
            )
            BottomBarItem.FOLDERS -> FolderListFeature(
                contentPadding = contentPadding,
            )
            BottomBarItem.SEARCH -> SearchScreenFeature(
                contentPadding = contentPadding,
            )
            BottomBarItem.SETTINGS -> SettingsFeature(
                versionName = BuildConfig.VERSION_NAME,
                contentPadding = contentPadding,
            )
        }
    }
}
