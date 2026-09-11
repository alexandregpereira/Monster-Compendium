/*
 * Copyright (C) 2024 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.app

import br.alexandregpereira.hunter.ads.consent.AdsConsentManager
import br.alexandregpereira.hunter.analytics.Analytics
import br.alexandregpereira.hunter.app.event.AppEventDispatcher
import br.alexandregpereira.hunter.localization.AppReactiveLocalization
import br.alexandregpereira.hunter.revenue.IsPremium
import br.alexandregpereira.hunter.revenue.RevenueSession
import br.alexandregpereira.hunter.state.UiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainViewModel(
    private val appLocalization: AppReactiveLocalization,
    private val appEventDispatcher: AppEventDispatcher,
    private val analytics: Analytics,
    private val revenueSession: RevenueSession,
    private val adsConsentManager: AdsConsentManager,
    private val isPremium: IsPremium,
) : UiModel<Unit>(Unit) {

    init {
        observeLanguageChanges()
        appEventDispatcher.observeEvents()
    }

    fun onStart() {
        analytics.track("App - started")
        revenueSession.initialize(apiKey = AppConfig.REVENUE_CAT_API_KEY)
        revenueSession.start()
        checkAdsConsent()
    }

    fun onStop() {
        analytics.track("App - stopped")
        revenueSession.stop()
    }

    fun onPause() {
        analytics.track("App - paused")
        revenueSession.stop()
    }

    fun onResume() {
        analytics.track("App - resumed")
        revenueSession.start()
        checkAdsConsent()
    }

    private fun checkAdsConsent() {
        flow {
            emit(isPremium())
        }.flowOn(Dispatchers.Default)
            .onEach { isPremium ->
                if (isPremium.not()) {
                    adsConsentManager.showConsentFormIfRequired()
                } else {
                    adsConsentManager.loadConsentInfo()
                }
            }
            .launchIn(scope)
    }

    private fun observeLanguageChanges() {
        appLocalization.languageFlow.onEach { language ->
            analytics.setUserProperty(name = "appLanguage", value = language.code)
        }.launchIn(scope)
    }

    fun onFileOpen(uri: String) {
        appEventDispatcher.onFileOpen(
            filePath = uri,
        )
    }
}
