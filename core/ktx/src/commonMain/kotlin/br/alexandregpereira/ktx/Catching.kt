package br.alexandregpereira.ktx

import kotlin.coroutines.cancellation.CancellationException

inline fun <T, R> T.runCatching(block: T.() -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (e: Throwable) {
        if (e is CancellationException) throw e
        Result.failure(e)
    }
}
