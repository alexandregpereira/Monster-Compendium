package br.alexandregpereira.hunter.app.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.alexandregpereira.hunter.folder.insert.FolderInsertFeature
import br.alexandregpereira.hunter.monster.content.MonsterContentManagerFeature
import br.alexandregpereira.hunter.monster.lore.registration.MonsterLoreRegistrationFeature
import br.alexandregpereira.hunter.monster.registration.MonsterRegistrationFeature
import br.alexandregpereira.hunter.settings.SettingsBottomSheets
import br.alexandregpereira.hunter.shareContent.ShareContentExportMonsterFeature
import br.alexandregpereira.hunter.shareContent.ShareContentImportFeature
import br.alexandregpereira.hunter.spell.compendium.SpellCompendiumFeature
import br.alexandregpereira.hunter.spell.detail.SpellDetailFeature
import br.alexandregpereira.hunter.sync.SyncFeature

@Composable
internal fun AppMainScreen(
    contentPadding: PaddingValues,
    content: @Composable () -> Unit,
) = Box(Modifier.fillMaxSize()) {
    content()
    MonsterContentManagerFeature(contentPadding = contentPadding)
    MonsterRegistrationFeature(contentPadding = contentPadding)
    MonsterLoreRegistrationFeature()
    SpellCompendiumFeature(contentPadding = contentPadding)
    SpellDetailFeature(contentPadding = contentPadding)
    FolderInsertFeature(contentPadding = contentPadding)
    ShareContentExportMonsterFeature(contentPadding = contentPadding)
    ShareContentImportFeature(contentPadding = contentPadding)
    SettingsBottomSheets()
    SyncFeature()
}