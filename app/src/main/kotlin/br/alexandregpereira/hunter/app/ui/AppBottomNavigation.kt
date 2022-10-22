/*
 * Copyright (C) 2022 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.app.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationDefaults
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.app.BottomBarItem
import br.alexandregpereira.hunter.ui.theme.HunterTheme

@Composable
fun BoxScope.AppBottomNavigation(
    showBottomBar: Boolean,
    bottomBarItemSelected: BottomBarItem,
    contentPadding: PaddingValues = PaddingValues(),
    onClick: (BottomBarItem) -> Unit = {}
) = HunterTheme {
    AnimatedVisibility(
        visible = showBottomBar,
        enter = slideInVertically { fullHeight -> fullHeight },
        exit = slideOutVertically { fullHeight -> fullHeight },
        modifier = Modifier.align(Alignment.BottomStart)
    ) {
        Surface(
            modifier = Modifier
                .background(MaterialTheme.colors.surface),
            elevation = BottomNavigationDefaults.Elevation
        ) {
            BottomNavigation(
                backgroundColor = MaterialTheme.colors.surface,
                elevation = 0.dp,
                modifier = Modifier.padding(bottom = contentPadding.calculateBottomPadding())
            ) {
                BottomBarItem.values().forEach { bottomBarItem ->
                    AppBottomNavigationItem(
                        indexSelected = bottomBarItemSelected.ordinal,
                        currentIndex = bottomBarItem.ordinal,
                        iconRes = bottomBarItem.iconRes,
                        nameRes = bottomBarItem.stringRes,
                        onClick = { onClick(bottomBarItem) }
                    )
                }
            }
        }
    }
}

@Composable
private fun RowScope.AppBottomNavigationItem(
    indexSelected: Int,
    currentIndex: Int,
    iconRes: Int,
    nameRes: Int,
    onClick: () -> Unit
) {
    BottomNavigationItem(
        selected = indexSelected == currentIndex,
        onClick = onClick,
        icon = {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = stringResource(nameRes)
            )
        },
        label = { Text(text = stringResource(nameRes)) }
    )
}
