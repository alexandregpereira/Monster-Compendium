package br.alexandregpereira.hunter.featureFlag

import java.net.UnknownHostException

internal actual fun Throwable.isNetworkFailure(): Boolean {
    var current: Throwable? = this
    var depth = 0
    while (current != null && depth < MAX_CAUSE_DEPTH) {
        if (current is UnknownHostException) return true
        current = current.cause
        depth++
    }
    return false
}
