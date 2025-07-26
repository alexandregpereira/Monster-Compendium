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

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.alexandregpereira.hunter.app.BottomBarItem
import br.alexandregpereira.hunter.app.BottomBarItemIcon
import br.alexandregpereira.hunter.folder.detail.FolderDetailFeature
import br.alexandregpereira.hunter.folder.list.FolderListFeature
import br.alexandregpereira.hunter.folder.preview.FolderPreviewFeature
import br.alexandregpereira.hunter.monster.compendium.MonsterCompendiumFeature
import br.alexandregpereira.hunter.search.SearchScreenFeature
import br.alexandregpereira.hunter.settings.SettingsFeature
import br.alexandregpereira.hunter.ui.compose.Window

@Composable
internal fun AppBottomNavigationTransition(
    bottomBarItemSelected: BottomBarItem?,
    contentPadding: PaddingValues,
    bottomBarItemSelectedIndex: Int,
    bottomBarItems: List<BottomBarItem>,
    onClick: (BottomBarItem) -> Unit,
) {
    Column(modifier = Modifier.padding(contentPadding)) {
        Window(Modifier.weight(1f)) {
            if (bottomBarItemSelected == null) return@Window
            Crossfade(
                targetState = bottomBarItemSelected,
                label = "BottomNavigationTransition"
            ) { item ->
                when (item.icon) {
                    BottomBarItemIcon.COMPENDIUM -> MonsterCompendiumFeature()

                    BottomBarItemIcon.FOLDERS -> {
                        FolderListFeature()
                        FolderDetailFeature()
                    }

                    BottomBarItemIcon.SEARCH -> SearchScreenFeature()

                    BottomBarItemIcon.SETTINGS -> SettingsFeature(
                        versionName = getVersionName(),
                    )
                }
            }
        }

        FolderPreviewFeature(
            modifier = Modifier,
        )

        AppBottomNavigation(
            showBottomBar = true,
            bottomBarItemSelectedIndex = bottomBarItemSelectedIndex,
            bottomBarItems = bottomBarItems,
            onClick = onClick,
        )
    }
}

internal expect fun getVersionName(): String
