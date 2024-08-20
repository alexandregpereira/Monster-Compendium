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

package br.alexandregpereira.hunter.spell.compendium.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.spell.compendium.EmptySpellCompendiumIntent
import br.alexandregpereira.hunter.spell.compendium.SpellCompendiumIntent
import br.alexandregpereira.hunter.spell.compendium.SpellCompendiumState
import br.alexandregpereira.hunter.ui.compose.AppFullScreen
import br.alexandregpereira.hunter.ui.compose.AppTextField

@Composable
internal fun SpellCompendiumScreen(
    state: SpellCompendiumState,
    contentPadding: PaddingValues,
    intent: SpellCompendiumIntent = EmptySpellCompendiumIntent(),
) = AppFullScreen(
    isOpen = state.isShowing,
    contentPaddingValues = contentPadding,
    onClose = intent::onClose
) {
    Column(
        modifier = Modifier.padding(contentPadding).padding(horizontal = 16.dp)
            .padding(bottom = 16.dp),
    ) {
        AppTextField(
            text = state.searchText,
            label = state.searchTextLabel,
            capitalize = false,
            modifier = Modifier.padding(start = 32.dp),
            onValueChange = intent::onSearchTextChange
        )

        SpellList(
            spellsGroupByLevel = state.spellsGroupByLevel,
            initialItemIndex = state.initialItemIndex,
            intent = intent
        )
    }
}
