package br.alexandregpereira.hunter.spell.detail

import br.alexandregpereira.hunter.spell.detail.event.SpellDetailEvent
import br.alexandregpereira.hunter.spell.detail.event.SpellDetailEventDispatcher
import br.alexandregpereira.hunter.spell.detail.event.SpellDetailEventListener
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

internal class SpellDetailEventManager : SpellDetailEventListener, SpellDetailEventDispatcher {

    private val _events: MutableSharedFlow<SpellDetailEvent> = MutableSharedFlow(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    override val events: Flow<SpellDetailEvent> = _events

    override fun dispatchEvent(event: SpellDetailEvent) {
        _events.tryEmit(event)
    }
}
