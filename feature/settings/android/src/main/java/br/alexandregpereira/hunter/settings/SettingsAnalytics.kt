package br.alexandregpereira.hunter.settings

import br.alexandregpereira.hunter.analytics.Analytics

internal class SettingsAnalytics(
    private val analytics: Analytics
) {

    fun logException(throwable: Throwable) {
        analytics.logException(throwable)
    }

    fun trackSaveButtonClick(value: SettingsViewState) {
        analytics.track(
            eventName = "Settings - save button click",
            params = mapOf(
                "imageBaseUrl" to value.imageBaseUrl,
                "alternativeSourceBaseUrl" to value.alternativeSourceBaseUrl,
            )
        )
    }

    fun trackManageMonsterContentClick() {
        analytics.track(
            eventName = "Settings - manage monster content click",
        )
    }

    fun trackLoadSettings(state: SettingsViewState) {
        analytics.track(
            eventName = "Settings - loaded",
            params = mapOf(
                "imageBaseUrl" to state.imageBaseUrl,
                "alternativeSourceBaseUrl" to state.alternativeSourceBaseUrl,
            )
        )
    }
}