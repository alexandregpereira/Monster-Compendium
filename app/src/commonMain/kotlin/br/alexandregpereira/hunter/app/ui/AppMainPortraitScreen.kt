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
internal fun AppMainPortraitScreen() = AppMainScreen {
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

    Box {
        MonsterDetailFeature()
        MonsterDetailBottomSheets()
    }
    MonsterLoreDetailFeature()
}
