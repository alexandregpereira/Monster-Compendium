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

package br.alexandregpereira.hunter.home.event

import br.alexandregpereira.hunter.event.v2.EventDispatcher
import kotlinx.coroutines.channels.Channel

sealed class HomeEvent {

    /**
     * Dispatched by the features when the content shown on the Home changes, like monsters, spells,
     * folders or extra contents being created, cloned, deleted or synced.
     *
     * @param wasCreatureDeleted true when a creature can have been deleted, like a monster deletion
     * or a sync, so the Home also reloads the recently viewed monsters shown.
     */
    data class OnContentChanged(val wasCreatureDeleted: Boolean = false) : HomeEvent()
}

/**
 * The buffer is unlimited, so an event isn't dropped when events are dispatched in sequence, since
 * each event can carry a different change.
 */
class HomeEventDispatcher : EventDispatcher<HomeEvent> by EventDispatcher(
    extraBufferCapacity = Channel.UNLIMITED,
)
