/*
 * Copyright (C) 2024 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.monster.compendium

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.monster.compendium.state.MonsterCompendiumStateHolder
import br.alexandregpereira.hunter.monster.compendium.ui.MonsterCompendiumScreen
import br.alexandregpereira.hunter.state.compose.rememberStateHolder
import br.alexandregpereira.hunter.ui.compose.AppFullScreen

@Composable
fun MonsterCompendiumFeature(
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    val stateHolder: MonsterCompendiumStateHolder = rememberStateHolder()
    val state by stateHolder.state.collectAsState()

    AppFullScreen(
        isOpen = state.isShowing,
        level = 1,
        showCloseButton = false,
        onClose = stateHolder::onClose,
    ) {
        MonsterCompendiumScreen(
            state = state,
            actionHandler = stateHolder,
            initialScrollItemPosition = stateHolder.initialScrollItemPosition,
            contentPadding = contentPadding,
            events = stateHolder,
        )
    }
}
