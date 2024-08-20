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

package br.alexandregpereira.hunter.monster.content.preview

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

internal class MonsterContentPreviewEventManager {

    private val _events: MutableSharedFlow<MonsterContentPreviewEvent> = MutableSharedFlow(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events: Flow<MonsterContentPreviewEvent> = _events

    private fun dispatchEvent(event: MonsterContentPreviewEvent) {
        _events.tryEmit(event)
    }

    fun show(sourceAcronym: String, title: String) {
        dispatchEvent(MonsterContentPreviewEvent.Show(sourceAcronym, title))
    }
}

internal sealed class MonsterContentPreviewEvent {
    data class Show(val sourceAcronym: String, val title: String) : MonsterContentPreviewEvent()
}
