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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.alexandregpereira.hunter.folder.insert.FolderInsertFeature
import br.alexandregpereira.hunter.monster.content.MonsterContentManagerFeature
import br.alexandregpereira.hunter.monster.registration.MonsterRegistrationFeature
import br.alexandregpereira.hunter.paywall.PaywallFeature
import br.alexandregpereira.hunter.settings.SettingsBottomSheets
import br.alexandregpereira.hunter.shareContent.ShareContentExportMonsterFeature
import br.alexandregpereira.hunter.shareContent.ShareContentImportFeature
import br.alexandregpereira.hunter.spell.compendium.SpellCompendiumFeature
import br.alexandregpereira.hunter.spell.detail.SpellDetailFeature
import br.alexandregpereira.hunter.spell.registration.SpellRegistrationFeature
import br.alexandregpereira.hunter.sync.SyncFeature
import br.alexandregpereira.hunter.ui.color.ColorPickerBottomSheet

@Composable
internal fun AppMainScreen(
    content: @Composable () -> Unit,
) = Column {
    Box(Modifier.fillMaxSize()) {
        content()
        MonsterContentManagerFeature()
        MonsterRegistrationFeature()
        SpellCompendiumFeature()
        SpellDetailFeature()
        SpellRegistrationFeature()
        FolderInsertFeature()
        ShareContentExportMonsterFeature()
        ShareContentImportFeature()
        SettingsBottomSheets()
        ColorPickerBottomSheet()
        SyncFeature()
        PaywallFeature()
    }
}
