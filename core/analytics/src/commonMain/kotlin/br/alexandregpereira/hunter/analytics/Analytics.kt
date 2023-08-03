package br.alexandregpereira.hunter.analytics

interface Analytics {

    fun track(eventName: String, params: Map<String, Any?> = emptyMap())

    fun logException(throwable: Throwable)
}

internal class EmptyAnalytics : Analytics {

    override fun track(eventName: String, params: Map<String, Any?>) {}

    override fun logException(throwable: Throwable) {}
}
