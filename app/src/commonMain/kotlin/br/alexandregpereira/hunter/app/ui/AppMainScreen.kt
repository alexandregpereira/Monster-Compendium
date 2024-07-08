package br.alexandregpereira.hunter.app.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.app.MainViewEvent
import br.alexandregpereira.hunter.app.MainViewEvent.BottomNavigationItemClick
import br.alexandregpereira.hunter.app.MainViewState
import br.alexandregpereira.hunter.detail.MonsterDetailFeature
import br.alexandregpereira.hunter.monster.registration.MonsterRegistrationFeature
import br.alexandregpereira.hunter.spell.compendium.SpellCompendiumFeature
import br.alexandregpereira.hunter.spell.detail.SpellDetailFeature
import br.alexandregpereira.hunter.sync.SyncFeature
import br.alexandregpereira.hunter.ui.util.BottomNavigationHeight

@Composable
internal fun AppMainScreen(
    state: MainViewState,
    contentPadding: PaddingValues,
    onEvent: (MainViewEvent) -> Unit
) {
    Box {
        val bottomBarNavigationSize by animateDpAsState(
            targetValue = if (state.showBottomBar) BottomNavigationHeight else 0.dp,
            label = "bottomBarNavigationSize",
        )
        val contentPaddingWithBottomBar = PaddingValues(
            top = contentPadding.calculateTopPadding(),
            bottom = contentPadding.calculateBottomPadding() + bottomBarNavigationSize,
        )

        BottomNavigationTransition(
            bottomBarItemSelected = state.bottomBarItemSelected,
            contentPadding = contentPaddingWithBottomBar
        )

        MonsterDetailFeature(
            contentPadding = contentPadding,
        )

        MonsterRegistrationFeature(contentPadding = contentPadding)

        SpellCompendiumFeature(
            contentPadding = contentPadding,
        )

        SpellDetailFeature(
            contentPadding = contentPadding,
        )

        AppBottomNavigation(
            showBottomBar = state.showBottomBar,
            bottomBarItemSelectedIndex = state.bottomBarItemSelectedIndex,
            bottomBarItems = state.bottomBarItems,
            contentPadding = contentPadding,
            onClick = { onEvent(BottomNavigationItemClick(item = it)) }
        )

        SyncFeature()
    }
}
