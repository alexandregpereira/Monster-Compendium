package br.alexandregpereira.hunter.app.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
internal fun AppMainWidthSplitScreen(
    state: MainViewState,
    contentPadding: PaddingValues,
    leftPanelFraction: Float = 0.7f,
    onEvent: (MainViewEvent) -> Unit
) = Box(Modifier.fillMaxSize()) {
    Row(Modifier.fillMaxSize()) {
        Box(Modifier.fillMaxHeight().weight(leftPanelFraction)) {
            AppBottomNavigationTransition(
                bottomBarItemSelected = state.bottomBarItemSelected,
                contentPadding = contentPadding,
                bottomBarItemSelectedIndex = state.bottomBarItemSelectedIndex,
                bottomBarItems = state.bottomBarItems,
                onClick = { onEvent(BottomNavigationItemClick(item = it)) },
            )

            FolderDetailFeature(
                contentPadding = contentPadding
            )
        }

        Box(Modifier.fillMaxHeight().weight(1 - leftPanelFraction)) {
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
        }
    }

    MonsterContentManagerFeature(
        contentPadding = contentPadding,
    )

    FolderInsertFeature(contentPadding = contentPadding)

    ShareContentExportMonsterFeature(contentPadding = contentPadding)

    ShareContentImportFeature(contentPadding = contentPadding)

    SyncFeature()
}
