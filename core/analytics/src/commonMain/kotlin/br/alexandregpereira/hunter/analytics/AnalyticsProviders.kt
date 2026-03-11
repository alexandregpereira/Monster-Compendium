package br.alexandregpereira.hunter.analytics

internal class AnalyticsProviders(
    private val firebaseAnalytics: Analytics,
    private val amplitudeAnalytics: Analytics,
) : Analytics {

    override fun track(
        eventName: String,
        params: Map<String, Any?>
    ) {
        amplitudeAnalytics.track(eventName, params)
        firebaseAnalytics.track(eventName, params)
    }

    override fun logException(throwable: Throwable) {
        firebaseAnalytics.logException(throwable)
    }
}
