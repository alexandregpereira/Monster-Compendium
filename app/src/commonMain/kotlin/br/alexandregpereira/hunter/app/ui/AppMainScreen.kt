package br.alexandregpereira.hunter.app.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.alexandregpereira.hunter.app.MainViewEvent
import br.alexandregpereira.hunter.app.MainViewEvent.BottomNavigationItemClick
import br.alexandregpereira.hunter.app.MainViewState
import br.alexandregpereira.hunter.detail.MonsterDetailFeature
import br.alexandregpereira.hunter.folder.detail.FolderDetailFeature
import br.alexandregpereira.hunter.folder.insert.FolderInsertFeature
import br.alexandregpereira.hunter.monster.content.MonsterContentManagerFeature
import br.alexandregpereira.hunter.monster.lore.detail.MonsterLoreDetailFeature
import br.alexandregpereira.hunter.monster.registration.MonsterRegistrationFeature
import br.alexandregpereira.hunter.shareContent.ShareContentExportMonsterFeature
import br.alexandregpereira.hunter.shareContent.ShareContentImportFeature
import br.alexandregpereira.hunter.spell.compendium.SpellCompendiumFeature
import br.alexandregpereira.hunter.spell.detail.SpellDetailFeature
import br.alexandregpereira.hunter.sync.SyncFeature

@Composable
internal fun AppMainScreen(
    state: MainViewState,
    contentPadding: PaddingValues,
    onEvent: (MainViewEvent) -> Unit
) {
    Box(Modifier.fillMaxSize()) {
        AppBottomNavigationTransition(
            bottomBarItemSelected = state.bottomBarItemSelected,
            contentPadding = contentPadding,
            bottomBarItemSelectedIndex = state.bottomBarItemSelectedIndex,
            bottomBarItems = state.bottomBarItems,
            onClick = { onEvent(BottomNavigationItemClick(item = it)) },
        )

        MonsterContentManagerFeature(
            contentPadding = contentPadding,
        )

        FolderDetailFeature(
            contentPadding = contentPadding
        )

        MonsterDetailFeature(
            contentPadding = contentPadding,
        )

        MonsterLoreDetailFeature(contentPadding = contentPadding)

        MonsterRegistrationFeature(contentPadding = contentPadding)

        SpellCompendiumFeature(
            contentPadding = contentPadding,
        )

        SpellDetailFeature(
            contentPadding = contentPadding,
        )

        FolderInsertFeature(contentPadding = contentPadding)

        ShareContentExportMonsterFeature(contentPadding = contentPadding)

        ShareContentImportFeature(contentPadding = contentPadding)

        SyncFeature()
    }
}
