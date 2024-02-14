package br.alexandregpereira.flow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class StateFlowWrapper<T : Any>(
    private val origin: StateFlow<T>,
    private val coroutineScope: CoroutineScope,
) : StateFlow<T> by origin {

    fun subscribe(block: (T) -> Unit) {
        origin.onEach(block).launchIn(coroutineScope)
    }
}

class SharedFlowWrapper<T : Any>(
    private val origin: SharedFlow<T>,
    private val coroutineScope: CoroutineScope,
) : SharedFlow<T> by origin {

    fun subscribe(block: (T) -> Unit) {
        origin.onEach(block).launchIn(coroutineScope)
    }
}

fun <T : Any> StateFlow<T>.wrap(
    coroutineScope: CoroutineScope
): StateFlowWrapper<T> = StateFlowWrapper(
    origin = this,
    coroutineScope = coroutineScope
)

fun <T : Any> SharedFlow<T>.wrap(
    coroutineScope: CoroutineScope
): SharedFlowWrapper<T> = SharedFlowWrapper(
    origin = this,
    coroutineScope = coroutineScope
)
