package br.alexandregpereira.hunter.spell.compendium

import androidx.lifecycle.ViewModel
import br.alexandregpereira.hunter.state.StateHolder

internal class SpellCompendiumViewModel(
    private val stateHolder: SpellCompendiumStateHolder,
) : ViewModel(),
    SpellCompendiumIntent by stateHolder,
    StateHolder<SpellCompendiumState> by stateHolder
