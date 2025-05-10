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

package br.alexandregpereira.hunter.state

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

interface ActionHandler<Action : Any> {

    val action: SharedFlow<Action>
}

interface MutableActionHandler<Action : Any> : ActionHandler<Action> {

    fun sendAction(action: Action)

    fun onActionHandlerClose()
}

fun <Action : Any> MutableActionHandler(): MutableActionHandler<Action> {
    return MutableActionHandlerImpl()
}

private class MutableActionHandlerImpl<Action : Any> : MutableActionHandler<Action> {

    private val coroutineScope = MainScope()

    private val _action = MutableSharedFlow<Action>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    override val action: SharedFlow<Action> = _action.asSharedFlow()

    override fun sendAction(action: Action) {
        _action.tryEmit(action)
    }

    override fun onActionHandlerClose() {
        coroutineScope.cancel()
    }
}
