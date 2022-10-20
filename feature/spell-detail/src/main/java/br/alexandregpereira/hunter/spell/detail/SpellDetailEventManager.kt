/*
 * Copyright (C) 2022 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
