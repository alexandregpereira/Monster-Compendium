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
