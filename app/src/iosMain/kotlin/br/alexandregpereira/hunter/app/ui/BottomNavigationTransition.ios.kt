package br.alexandregpereira.hunter.app.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.alexandregpereira.hunter.app.BottomBarItem
import br.alexandregpereira.hunter.app.BottomBarItemIcon
import br.alexandregpereira.hunter.monster.compendium.MonsterCompendiumFeature
import br.alexandregpereira.hunter.ui.compose.Window

@Composable
actual fun BottomNavigationTransition(
    bottomBarItemSelected: BottomBarItem?,
    contentPadding: PaddingValues
) {
    if (bottomBarItemSelected == null) return
    Crossfade(targetState = bottomBarItemSelected, label = "BottomNavigationTransition") { item ->
        when (item.icon) {
            BottomBarItemIcon.COMPENDIUM -> MonsterCompendiumFeature(
                contentPadding = contentPadding,
            )
            BottomBarItemIcon.FOLDERS -> Window(Modifier.fillMaxSize()) {

            }
            BottomBarItemIcon.SEARCH -> Window(Modifier.fillMaxSize()) {

            }
            BottomBarItemIcon.SETTINGS -> Window(Modifier.fillMaxSize()) {

            }
        }
    }
}
