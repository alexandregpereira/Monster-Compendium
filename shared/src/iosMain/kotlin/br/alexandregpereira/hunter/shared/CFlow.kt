package br.alexandregpereira.hunter.shared

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

fun interface Closeable {
    fun close()
}

class CFlow<T: Any> internal constructor(private val origin: Flow<T>) : Flow<T> by origin {
    fun collect(block: (T) -> Unit): Closeable {
        val job = Job()

        onEach {
            block(it)
        }.launchIn(CoroutineScope(Dispatchers.Main.immediate + job))

        return Closeable { job.cancel() }
    }
}

fun <T: Any> Flow<T>.wrap(): CFlow<T> = CFlow(this)
