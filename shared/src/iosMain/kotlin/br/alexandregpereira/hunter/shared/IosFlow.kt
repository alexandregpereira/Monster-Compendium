package br.alexandregpereira.hunter.shared

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class IosFlow<T: Any> internal constructor(
    private val origin: Flow<T>,
    private val coroutineScope: CoroutineScope,
) {

    fun collect(block: (T) -> Unit) {
        origin.onEach(block).launchIn(coroutineScope)
    }
}

fun <T: Any> Flow<T>.iosFlow(coroutineScope: CoroutineScope): IosFlow<T> = IosFlow(
    origin = this,
    coroutineScope = coroutineScope
)
