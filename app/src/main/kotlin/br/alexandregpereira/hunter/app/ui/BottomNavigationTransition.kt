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

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import br.alexandregpereira.hunter.app.BottomBarItem
import br.alexandregpereira.hunter.monster.compendium.MonsterCompendiumFeature
import br.alexandregpereira.hunter.search.SearchScreenFeature
import br.alexandregpereira.hunter.settings.SettingsFeature

@Composable
fun BottomNavigationTransition(
    bottomBarItemSelected: BottomBarItem,
    contentPadding: PaddingValues = PaddingValues()
) {
    Crossfade(targetState = bottomBarItemSelected) { index ->
        when (index) {
            BottomBarItem.COMPENDIUM -> MonsterCompendiumFeature(
                contentPadding = contentPadding,
            )
            BottomBarItem.SEARCH -> SearchScreenFeature(
                contentPadding = contentPadding,
            )
            BottomBarItem.SETTINGS -> SettingsFeature(
                contentPadding = contentPadding,
            )
        }
    }
}
