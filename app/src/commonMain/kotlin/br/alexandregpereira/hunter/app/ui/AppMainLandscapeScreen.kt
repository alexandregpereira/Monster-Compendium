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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.alexandregpereira.hunter.app.AppConfig
import br.alexandregpereira.hunter.detail.MonsterDetailBottomSheets
import br.alexandregpereira.hunter.detail.MonsterDetailFeature
import br.alexandregpereira.hunter.folder.detail.FolderDetailFeature
import br.alexandregpereira.hunter.folder.list.FolderListFeature
import br.alexandregpereira.hunter.folder.preview.FolderPreviewFeature
import br.alexandregpereira.hunter.home.HomeFeature
import br.alexandregpereira.hunter.monster.compendium.MonsterCompendiumFeature
import br.alexandregpereira.hunter.monster.lore.detail.MonsterLoreDetailFeature
import br.alexandregpereira.hunter.search.SearchScreenFeature
import br.alexandregpereira.hunter.settings.SettingsFeature

@Composable
internal fun AppMainLandscapeScreen(
    leftPanelFraction: Float = 0.7f,
) = AppMainScreen {
    Row(Modifier.fillMaxSize()) {
        Box(Modifier.fillMaxHeight().weight(leftPanelFraction)) {
            Column {
                Box(
                    modifier = Modifier.weight(1f),
                ) {
                    HomeFeature()
                    MonsterCompendiumFeature()
                    SearchScreenFeature()
                    FolderListFeature()
                    FolderDetailFeature()
                    SettingsFeature(
                        versionName = AppConfig.VERSION_NAME,
                    )
                }
                FolderPreviewFeature(
                    modifier = Modifier,
                )
            }
        }

        Box(Modifier.fillMaxHeight().weight(1 - leftPanelFraction)) {
            MonsterDetailFeature()
            MonsterLoreDetailFeature()
        }
    }
    MonsterDetailBottomSheets()
}
