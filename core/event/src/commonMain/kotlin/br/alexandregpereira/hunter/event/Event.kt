package br.alexandregpereira.hunter.event

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

interface EventDispatcher<Event> {

    fun dispatchEvent(event: Event)
}

interface EventResultDispatcher<Event, Result> {

    fun dispatchEventResult(event: Event): Flow<Result>
}

interface EventListener<Event> {

    val events: Flow<Event>
}

interface EventManager<Event> : EventDispatcher<Event>, EventListener<Event>

fun <Event> EventManager(): EventManager<Event> = DefaultEventManager()

private class DefaultEventManager<Event> : EventManager<Event> {

    private val _events: MutableSharedFlow<Event> = MutableSharedFlow(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    override val events: Flow<Event> = _events

    override fun dispatchEvent(event: Event) {
        _events.tryEmit(event)
    }
}