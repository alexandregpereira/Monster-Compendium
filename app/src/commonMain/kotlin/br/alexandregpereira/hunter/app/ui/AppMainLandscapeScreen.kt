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
import br.alexandregpereira.hunter.detail.MonsterDetailBottomSheets
import br.alexandregpereira.hunter.detail.MonsterDetailFeature
import br.alexandregpereira.hunter.monster.lore.detail.MonsterLoreDetailFeature

@Composable
internal fun AppMainLandscapeScreen(
    state: MainViewState,
    contentPadding: PaddingValues,
    leftPanelFraction: Float = 0.7f,
    onEvent: (MainViewEvent) -> Unit
) = AppMainScreen(contentPadding) {
    Row(Modifier.fillMaxSize()) {
        Box(Modifier.fillMaxHeight().weight(leftPanelFraction)) {
            AppBottomNavigationTransition(
                bottomBarItemSelected = state.bottomBarItemSelected,
                contentPadding = contentPadding,
                bottomBarItemSelectedIndex = state.bottomBarItemSelectedIndex,
                bottomBarItems = state.bottomBarItems,
                onClick = { onEvent(BottomNavigationItemClick(item = it)) },
            )
        }

        Box(Modifier.fillMaxHeight().weight(1 - leftPanelFraction)) {
            MonsterDetailFeature(contentPadding = contentPadding)
            MonsterLoreDetailFeature(contentPadding = contentPadding)
        }
    }
    MonsterDetailBottomSheets()
}
