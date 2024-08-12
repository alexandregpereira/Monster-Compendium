package br.alexandregpereira.hunter.app.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import br.alexandregpereira.hunter.app.MainViewEvent
import br.alexandregpereira.hunter.app.MainViewEvent.BottomNavigationItemClick
import br.alexandregpereira.hunter.app.MainViewState
import br.alexandregpereira.hunter.detail.MonsterDetailBottomSheets
import br.alexandregpereira.hunter.detail.MonsterDetailFeature
import br.alexandregpereira.hunter.folder.detail.FolderDetailFeature
import br.alexandregpereira.hunter.monster.lore.detail.MonsterLoreDetailFeature

@Composable
internal fun AppMainPortraitScreen(
    state: MainViewState,
    contentPadding: PaddingValues,
    onEvent: (MainViewEvent) -> Unit
) = AppMainScreen(contentPadding) {
    AppBottomNavigationTransition(
        bottomBarItemSelected = state.bottomBarItemSelected,
        contentPadding = contentPadding,
        bottomBarItemSelectedIndex = state.bottomBarItemSelectedIndex,
        bottomBarItems = state.bottomBarItems,
        onClick = { onEvent(BottomNavigationItemClick(item = it)) },
    )
    FolderDetailFeature(contentPadding = contentPadding)
    Box {
        MonsterDetailFeature(contentPadding = contentPadding)
        MonsterDetailBottomSheets()
    }
    MonsterLoreDetailFeature(contentPadding = contentPadding)
}
