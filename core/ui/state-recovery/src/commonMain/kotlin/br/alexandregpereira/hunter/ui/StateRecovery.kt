package br.alexandregpereira.hunter.ui

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

interface StateRecovery<State> {
    val state: State
    val onStateChanges: Flow<State>

    fun saveState(state: State)
}

fun <State> StateRecovery(
    initialState: State,
): StateRecovery<State> = DefaultMonsterCompendiumStateRecovery(initialState)

fun <State> State.saveState(
    recovery: StateRecovery<State>
): State {
    recovery.saveState(this)
    return this
}

private class DefaultMonsterCompendiumStateRecovery<State>(
    initialState: State,
) : StateRecovery<State> {

    private val dispatcher: MutableSharedFlow<State> = MutableSharedFlow(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    override val onStateChanges: Flow<State> = dispatcher.asSharedFlow()

    private var _state: State = initialState
    override val state: State
        get() = _state

    override fun saveState(state: State) {
        if (_state == state) return
        _state = state
        dispatcher.tryEmit(state)
    }
}
