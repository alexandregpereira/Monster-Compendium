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

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class UiModel<State>(
    initialState: State
) : StateHolder<State> {

    protected val scope = CoroutineScope(
        SupervisorJob() + Dispatchers.Main.immediate
    )

    private val _state = MutableStateFlow(initialState)
    override val state: StateFlow<State> = _state

    protected fun setState(block: State.() -> State) {
        _state.value = state.value.block()
    }

    fun onCleared() {
        scope.cancel()
    }
}

interface StateHolder<State> {

    val state: StateFlow<State>
}

interface MutableStateHolder<State>: StateHolder<State> {

    fun setState(block: State.() -> State)
}

fun <State> MutableStateHolder(initialState: State): MutableStateHolder<State> {
    return DefaultStateHolderImpl(initialState)
}

fun <State> MutableStateHolder(
    stateRecovery: StateRecovery<State>
): MutableStateHolder<State> {
    return DefaultStateHolderWithRecovery(stateRecovery)
}

private class DefaultStateHolderImpl<State>(
    initialState: State
): MutableStateHolder<State> {

    private val _state = MutableStateFlow(initialState)
    override val state: StateFlow<State> = _state

    override fun setState(block: State.() -> State) {
        _state.value = state.value.block()
    }
}

private class DefaultStateHolderWithRecovery<State>(
    private val stateRecovery: StateRecovery<State>
): MutableStateHolder<State> {

    private val stateHolderDelegate = DefaultStateHolderImpl(stateRecovery.getState())

    override val state: StateFlow<State> = stateHolderDelegate.state

    override fun setState(block: State.() -> State) {
        stateHolderDelegate.setState {
            block().also {
                stateRecovery.saveState(it)
            }
        }
    }
}
