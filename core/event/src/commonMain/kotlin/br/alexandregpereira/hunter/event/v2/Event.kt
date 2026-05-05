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

fun <Event> EventDispatcher(
    extraBufferCapacity: Int = 10,
    onBufferOverflow: BufferOverflow = BufferOverflow.DROP_OLDEST,
): EventDispatcher<Event> = DefaultEventManager(extraBufferCapacity, onBufferOverflow)

private class DefaultEventManager<Event>(
    extraBufferCapacity: Int = 10,
    onBufferOverflow: BufferOverflow = BufferOverflow.DROP_OLDEST,
) : EventDispatcher<Event> {

    private val _events: MutableSharedFlow<Event> = MutableSharedFlow(
        extraBufferCapacity = extraBufferCapacity,
        onBufferOverflow = onBufferOverflow,
    )
    override val events: Flow<Event> = _events

    override fun dispatchEvent(event: Event) {
        _events.tryEmit(event)
    }
}
