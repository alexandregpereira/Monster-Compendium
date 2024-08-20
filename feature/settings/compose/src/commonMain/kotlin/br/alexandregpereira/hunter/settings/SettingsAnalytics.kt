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

    fun trackAdvancedSettingsClick() {
        analytics.track(
            eventName = "Settings - advanced settings click",
        )
    }

    fun trackSettingsClick() {
        analytics.track(
            eventName = "Settings - settings click",
        )
    }

    fun trackCommonSettingSaveButtonClick(settingsState: SettingsState) {
        analytics.track(
            eventName = "Settings - common setting save click",
            params = mapOf(
                "language" to settingsState.language.code,
            )
        )
    }

    fun trackLanguageChange(language: SettingsLanguageState) {
        analytics.track(
            eventName = "Settings - language change",
            params = mapOf(
                "language" to language.code,
            )
        )
    }

    fun trackOpenGitHubProjectClick() {
        analytics.track(
            eventName = "Settings - open GitHub project click",
        )
    }

    fun trackImportContentClick() {
        analytics.track(
            eventName = "Settings - import content click",
        )
    }

    fun trackAppearanceSettingsSaveClick() {
        analytics.track(
            eventName = "Settings - appearance settings save click",
        )
    }

    fun trackAppearanceSettingsClick() {
        analytics.track(
            eventName = "Settings - appearance settings click",
        )
    }

    fun trackDonateClick() {
        analytics.track(
            eventName = "Settings - donate click",
        )
    }

    fun trackPixCodeCopyClick() {
        analytics.track(
            eventName = "Donate - pix code copy click",
        )
    }

    fun trackBuyMeCoffeeClick() {
        analytics.track(
            eventName = "Donate - buy me coffee click",
        )
    }

    fun trackPixKeyCopyClick() {
        analytics.track(
            eventName = "Donate - pix key copy click",
        )
    }
}