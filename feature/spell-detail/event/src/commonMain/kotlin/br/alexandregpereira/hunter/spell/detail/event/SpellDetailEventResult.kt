package br.alexandregpereira.hunter.spell.detail.event

import br.alexandregpereira.hunter.event.v2.EventDispatcher

sealed class SpellDetailResult {
    data class OnChanged(val spellIndex: String) : SpellDetailResult()
}

class SpellDetailResultDispatcher : EventDispatcher<SpellDetailResult> by EventDispatcher(
    extraBufferCapacity = 1
)
