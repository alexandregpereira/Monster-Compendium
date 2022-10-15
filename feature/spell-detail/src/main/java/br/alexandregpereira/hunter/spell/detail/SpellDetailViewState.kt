package br.alexandregpereira.hunter.spell.detail

import br.alexandregpereira.hunter.spell.detail.ui.SpellState

internal data class SpellDetailViewState(
    val spell: SpellState? = null,
    val showDetail: Boolean = false,
) {

    companion object {
        val INITIAL = SpellDetailViewState()
    }
}

internal fun SpellDetailViewState.changeSpell(spellState: SpellState): SpellDetailViewState {
    return copy(spell = spellState, showDetail = true)
}

internal fun SpellDetailViewState.hideDetail(): SpellDetailViewState {
    return copy(showDetail = false)
}
