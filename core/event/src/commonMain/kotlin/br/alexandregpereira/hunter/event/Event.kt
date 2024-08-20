/*
 * Copyright (C) 2024 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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