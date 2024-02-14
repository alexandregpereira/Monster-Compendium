/*
 * Copyright 2023 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.alexandregpereira.hunter.state

import br.alexandregpereira.flow.SharedFlowWrapper
import br.alexandregpereira.flow.wrap
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow

interface ActionHandler<Action : Any> {

    val action: SharedFlowWrapper<Action>
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
    override val action: SharedFlowWrapper<Action> = _action.wrap(coroutineScope)

    override fun sendAction(action: Action) {
        _action.tryEmit(action)
    }

    override fun onActionHandlerClose() {
        coroutineScope.cancel()
    }
}
