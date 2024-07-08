package br.alexandregpereira.hunter.spell.compendium.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.spell.compendium.EmptySpellCompendiumIntent
import br.alexandregpereira.hunter.spell.compendium.SpellCompendiumIntent
import br.alexandregpereira.hunter.spell.compendium.SpellCompendiumState
import br.alexandregpereira.hunter.ui.compose.AppTextField
import br.alexandregpereira.hunter.ui.compose.BackHandler
import br.alexandregpereira.hunter.ui.compose.SwipeVerticalToDismiss
import br.alexandregpereira.hunter.ui.compose.Window

@Composable
internal fun SpellCompendiumScreen(
    state: SpellCompendiumState,
    contentPadding: PaddingValues,
    intent: SpellCompendiumIntent = EmptySpellCompendiumIntent(),
) {
    BackHandler(enabled = state.isShowing, onBack = intent::onClose)

    SwipeVerticalToDismiss(
        visible = state.isShowing,
        onClose = intent::onClose,
    ) {
        Window(
            modifier = Modifier.fillMaxSize(),
        ) {
            Column(
                modifier = Modifier.padding(
                    top = contentPadding.calculateTopPadding(),
                    bottom = contentPadding.calculateBottomPadding(),
                    start = 16.dp,
                    end = 16.dp,
                ),
            ) {
                AppTextField(
                    text = state.searchText,
                    label = state.searchTextLabel,
                    capitalize = false,
                    onValueChange = intent::onSearchTextChange
                )

                SpellList(
                    spellsGroupByLevel = state.spellsGroupByLevel,
                    initialItemIndex = state.initialItemIndex,
                    intent = intent
                )
            }
        }
    }
}
