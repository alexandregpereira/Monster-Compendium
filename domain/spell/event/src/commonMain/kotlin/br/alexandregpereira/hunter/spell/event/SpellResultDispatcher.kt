package br.alexandregpereira.hunter.spell.event

import br.alexandregpereira.hunter.event.v2.EventDispatcher
import br.alexandregpereira.hunter.event.v2.EventListener
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

sealed class SpellResult {
    data class OnChanged(val spellIndex: String) : SpellResult()
    data class OnAdded(val spellIndex: String) : SpellResult()
}

class SpellResultDispatcher : EventDispatcher<SpellResult> by EventDispatcher(
    extraBufferCapacity = 1,
)

fun EventListener<SpellResult>.collectOnChanged(
    onAction: suspend (SpellResult.OnChanged) -> Unit
): Flow<SpellResult.OnChanged> = events.map { it as? SpellResult.OnChanged }
    .filterNotNull().onEach(onAction)
