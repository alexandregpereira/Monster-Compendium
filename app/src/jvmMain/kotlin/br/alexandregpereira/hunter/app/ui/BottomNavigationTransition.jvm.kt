package br.alexandregpereira.hunter.app.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import br.alexandregpereira.hunter.app.BottomBarItem

@Composable
internal actual fun BottomNavigationTransition(
    bottomBarItemSelected: BottomBarItem?,
    contentPadding: PaddingValues
) = AppBottomNavigationTransition(bottomBarItemSelected, contentPadding)
