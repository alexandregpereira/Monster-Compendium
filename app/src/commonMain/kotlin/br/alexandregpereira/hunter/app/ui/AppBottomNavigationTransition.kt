package br.alexandregpereira.hunter.app.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import br.alexandregpereira.hunter.app.BottomBarItem
import br.alexandregpereira.hunter.app.BottomBarItemIcon
import br.alexandregpereira.hunter.folder.list.FolderListFeature
import br.alexandregpereira.hunter.monster.compendium.MonsterCompendiumFeature
import br.alexandregpereira.hunter.search.SearchScreenFeature
import br.alexandregpereira.hunter.settings.SettingsFeature

@Composable
internal fun AppBottomNavigationTransition(
    bottomBarItemSelected: BottomBarItem?,
    contentPadding: PaddingValues
) {
    if (bottomBarItemSelected == null) return
    Crossfade(targetState = bottomBarItemSelected, label = "BottomNavigationTransition") { item ->
        when (item.icon) {
            BottomBarItemIcon.COMPENDIUM -> MonsterCompendiumFeature(
                contentPadding = contentPadding,
            )
            BottomBarItemIcon.FOLDERS -> FolderListFeature(
                contentPadding = contentPadding,
            )
            BottomBarItemIcon.SEARCH -> SearchScreenFeature(
                contentPadding = contentPadding,
            )
            BottomBarItemIcon.SETTINGS -> SettingsFeature(
                versionName = "",
                contentPadding = contentPadding,
            )
        }
    }
}
