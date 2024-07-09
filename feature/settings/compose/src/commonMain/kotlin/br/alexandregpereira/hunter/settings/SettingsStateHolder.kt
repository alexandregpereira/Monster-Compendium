/*
 * Copyright 2022 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.alexandregpereira.hunter.settings

import br.alexandregpereira.hunter.domain.settings.GetAlternativeSourceJsonUrlUseCase
import br.alexandregpereira.hunter.domain.settings.GetMonsterImageJsonUrlUseCase
import br.alexandregpereira.hunter.domain.settings.SaveLanguageUseCase
import br.alexandregpereira.hunter.domain.settings.SaveUrlsUseCase
import br.alexandregpereira.hunter.event.EventDispatcher
import br.alexandregpereira.hunter.event.systembar.BottomBarEvent
import br.alexandregpereira.hunter.event.systembar.dispatchAddTopContentEvent
import br.alexandregpereira.hunter.event.systembar.dispatchRemoveTopContentEvent
import br.alexandregpereira.hunter.localization.AppLocalization
import br.alexandregpereira.hunter.localization.Language
import br.alexandregpereira.hunter.monster.content.event.MonsterContentManagerEvent.Show
import br.alexandregpereira.hunter.monster.content.event.MonsterContentManagerEventDispatcher
import br.alexandregpereira.hunter.state.UiModel
import br.alexandregpereira.hunter.sync.event.SyncEventDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.zip

internal class SettingsStateHolder(
    private val getMonsterImageJsonUrl: GetMonsterImageJsonUrlUseCase,
    private val getAlternativeSourceJsonUrl: GetAlternativeSourceJsonUrlUseCase,
    private val saveUrls: SaveUrlsUseCase,
    private val monsterContentManagerEventDispatcher: MonsterContentManagerEventDispatcher,
    private val dispatcher: CoroutineDispatcher,
    private val syncEventDispatcher: SyncEventDispatcher,
    private val analytics: SettingsAnalytics,
    private val bottomBarEventDispatcher: EventDispatcher<BottomBarEvent>,
    private val appLocalization: AppLocalization,
    private val saveLanguage: SaveLanguageUseCase,
) : UiModel<SettingsViewState>(SettingsViewState()), SettingsViewIntent {

    private val strings: SettingsStrings
        get() = getSettingsStrings(appLocalization.getLanguage())
    private var originalSettingsState: SettingsState = SettingsState()

    init {
        load()
    }

    override fun onImageBaseUrlChange(value: String) {
        setState { copy(imageBaseUrl = value) }
    }

    override fun onAlternativeSourceBaseUrlChange(value: String) {
        setState { copy(alternativeSourceBaseUrl = value) }
    }

    override fun onSaveButtonClick() {
        analytics.trackSaveButtonClick(state.value)
        saveUrls(
            imageBaseUrl = state.value.imageBaseUrl,
            alternativeSourceBaseUrl = state.value.alternativeSourceBaseUrl
        ).flowOn(dispatcher)
            .onEach {
                closeAdvancedSettings()
                syncEventDispatcher.startSync()
            }
            .launchIn(scope)
    }

    override fun onManageMonsterContentClick() {
        analytics.trackManageMonsterContentClick()
        monsterContentManagerEventDispatcher.dispatchEvent(Show)
    }

    override fun onAdvancedSettingsClick() {
        analytics.trackAdvancedSettingsClick()
        setState { copy(advancedSettingsOpened = true) }
        bottomBarEventDispatcher.dispatchAddTopContentEvent(topContentId = ADVANCED_SETTINGS_CONTENT)
    }

    override fun onAdvancedSettingsCloseClick() = closeAdvancedSettings()

    override fun onSettingsClick() {
        analytics.trackSettingsClick()
        setState { copy(settingsOpened = true) }
        bottomBarEventDispatcher.dispatchAddTopContentEvent(topContentId = SETTINGS_CONTENT)
    }

    override fun onSettingsCloseClick() {
        setState { copy(settingsOpened = false) }
        bottomBarEventDispatcher.dispatchRemoveTopContentEvent(topContentId = SETTINGS_CONTENT)
    }

    override fun onSettingsSaveClick() {
        analytics.trackCommonSettingSaveButtonClick(state.value.settingsState)
        setState { copy(settingsOpened = false) }
        bottomBarEventDispatcher.dispatchRemoveTopContentEvent(topContentId = SETTINGS_CONTENT)
        saveLanguage(state.value.settingsState.language.code)
            .flowOn(dispatcher)
            .onEach {
                if (originalSettingsState.language != state.value.settingsState.language) {
                    syncEventDispatcher.startSync()
                    originalSettingsState = state.value.settingsState
                }
                setState { copy(strings = this@SettingsStateHolder.strings)  }
            }
            .launchIn(scope)
    }

    override fun onLanguageChange(language: SettingsLanguageState) {
        analytics.trackLanguageChange(language)
        setState { copy(settingsState = settingsState.copy(language = language)) }
    }

    private fun load() {
        getMonsterImageJsonUrl()
            .zip(getAlternativeSourceJsonUrl()) { imageBaseUrl, alternativeSourceBaseUrl ->
                state.value.copy(
                    imageBaseUrl = imageBaseUrl,
                    alternativeSourceBaseUrl = alternativeSourceBaseUrl,
                    strings = this@SettingsStateHolder.strings,
                    settingsState = SettingsState(
                        languages = Language.entries.map { it.asState() },
                        language = appLocalization.getLanguage().asState()
                    )
                )
            }
            .flowOn(dispatcher)
            .catch {
                analytics.logException(it)
            }
            .onEach { state ->
                analytics.trackLoadSettings(state)
                originalSettingsState = state.settingsState
                setState { state }
            }
            .launchIn(scope)
    }

    private fun closeAdvancedSettings() {
        setState { copy(advancedSettingsOpened = false) }
        bottomBarEventDispatcher.dispatchRemoveTopContentEvent(topContentId = ADVANCED_SETTINGS_CONTENT)
    }

    private fun Language.asState(): SettingsLanguageState {
        val string =  when (this) {
            Language.ENGLISH -> "English (United States)"
            Language.PORTUGUESE -> "Português (Brasil)"
        }

        return SettingsLanguageState(
            code = this.code,
            value = string
        )
    }

    private companion object {
        private const val SETTINGS_CONTENT = "Settings"
        private const val ADVANCED_SETTINGS_CONTENT = "AdvancedSettings"
    }
}
