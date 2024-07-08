package br.alexandregpereira.hunter.spell.compendium

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import br.alexandregpereira.hunter.spell.compendium.ui.SpellCompendiumScreen
import org.koin.compose.koinInject

@Composable
fun SpellCompendiumFeature(
    contentPadding: PaddingValues = PaddingValues(),
) {
    val viewModel: SpellCompendiumStateHolder = koinInject()
    SpellCompendiumScreen(
        state = viewModel.state.collectAsState().value,
        contentPadding = contentPadding,
        intent = viewModel,
    )
}
