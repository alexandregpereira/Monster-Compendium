package br.alexandregpereira.hunter.event.v2

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

interface EventDispatcher<Event> : EventListener<Event> {

    fun dispatchEvent(event: Event)
}

interface EventListener<Event> {

    val events: Flow<Event>
}

fun <Event> EventDispatcher(): EventDispatcher<Event> = DefaultEventManager()

private class DefaultEventManager<Event> : EventDispatcher<Event> {

    private val _events: MutableSharedFlow<Event> = MutableSharedFlow(
        extraBufferCapacity = 10,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    override val events: Flow<Event> = _events

    override fun dispatchEvent(event: Event) {
        _events.tryEmit(event)
    }
}
