package br.alexandregpereira.hunter.app.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.alexandregpereira.hunter.app.BottomBarItem
import br.alexandregpereira.hunter.app.BottomBarItemIcon
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
    Column {
        Window(Modifier.weight(1f).padding(bottom = contentPadding.calculateBottomPadding())) {
            if (bottomBarItemSelected == null) return@Window
            Crossfade(
                targetState = bottomBarItemSelected,
                label = "BottomNavigationTransition"
            ) { item ->
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
                        versionName = getVersionName(),
                        contentPadding = contentPadding,
                    )
                }
            }
        }

        FolderPreviewFeature(
            contentPadding = contentPadding,
            modifier = Modifier,
        )

        AppBottomNavigation(
            showBottomBar = true,
            bottomBarItemSelectedIndex = bottomBarItemSelectedIndex,
            bottomBarItems = bottomBarItems,
            contentPadding = contentPadding,
            onClick = onClick,
        )
    }
}

internal expect fun getVersionName(): String
