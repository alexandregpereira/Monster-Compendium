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

import br.alexadregpereira.hunter.shareContent.event.ShareContentEvent
import br.alexadregpereira.hunter.shareContent.event.ShareContentEventDispatcher
import br.alexandregpereira.hunter.domain.settings.AppearanceSettings
import br.alexandregpereira.hunter.domain.settings.GetAlternativeSourceJsonUrlUseCase
import br.alexandregpereira.hunter.domain.settings.GetMonsterImageJsonUrlUseCase
import br.alexandregpereira.hunter.domain.settings.SaveLanguageUseCase
import br.alexandregpereira.hunter.domain.settings.SaveUrlsUseCase
import br.alexandregpereira.hunter.localization.AppLocalization
import br.alexandregpereira.hunter.localization.Language
import br.alexandregpereira.hunter.monster.content.event.MonsterContentManagerEvent.Show
import br.alexandregpereira.hunter.monster.content.event.MonsterContentManagerEventDispatcher
import br.alexandregpereira.hunter.monster.event.MonsterEvent
import br.alexandregpereira.hunter.monster.event.MonsterEventDispatcher
import br.alexandregpereira.hunter.settings.domain.ApplyAppearanceSettings
import br.alexandregpereira.hunter.settings.domain.GetAppearanceSettingsFromMonsters
import br.alexandregpereira.hunter.state.UiModel
import br.alexandregpereira.hunter.sync.event.SyncEventDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.zip

internal class SettingsStateHolder(
    private val getMonsterImageJsonUrl: GetMonsterImageJsonUrlUseCase,
    private val getAlternativeSourceJsonUrl: GetAlternativeSourceJsonUrlUseCase,
    private val saveUrls: SaveUrlsUseCase,
    private val monsterContentManagerEventDispatcher: MonsterContentManagerEventDispatcher,
    private val dispatcher: CoroutineDispatcher,
    private val syncEventDispatcher: SyncEventDispatcher,
    private val monsterEventDispatcher: MonsterEventDispatcher,
    private val shareContentEventDispatcher: ShareContentEventDispatcher,
    private val analytics: SettingsAnalytics,
    private val appLocalization: AppLocalization,
    private val saveLanguage: SaveLanguageUseCase,
    private val getAppearanceSettings: GetAppearanceSettingsFromMonsters,
    private val applyAppearanceSettings: ApplyAppearanceSettings,
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
    }

    override fun onAdvancedSettingsCloseClick() = closeAdvancedSettings()

    override fun onSettingsClick() {
        analytics.trackSettingsClick()
        setState { copy(settingsOpened = true) }
    }

    override fun onSettingsCloseClick() {
        setState { copy(settingsOpened = false) }
    }

    override fun onSettingsSaveClick() {
        analytics.trackCommonSettingSaveButtonClick(state.value.settingsState)
        setState { copy(settingsOpened = false) }
        saveLanguage(state.value.settingsState.language.code)
            .flowOn(dispatcher)
            .onEach {
                if (originalSettingsState.language != state.value.settingsState.language) {
                    syncEventDispatcher.startSync()
                    originalSettingsState = state.value.settingsState
                }
                setState { copy(strings = this@SettingsStateHolder.strings) }
            }
            .launchIn(scope)
    }

    override fun onLanguageChange(language: SettingsLanguageState) {
        analytics.trackLanguageChange(language)
        setState { copy(settingsState = settingsState.copy(language = language)) }
    }

    override fun onAppearanceSettingsClick() {
        fillAppearanceSettingsState()
        setState { copy(appearanceSettingsOpened = true) }
    }

    override fun onAppearanceSettingsCloseClick() {
        setState { copy(appearanceSettingsOpened = false) }
    }

    override fun onAppearanceSettingsSaveClick() {
        onAppearanceSettingsCloseClick()
        val appearanceState = state.value.appearanceState
        flow {
            AppearanceSettings(
                forceLightImageBackground = appearanceState.forceLightImageBackground,
                defaultLightBackground = appearanceState.defaultLightBackground,
                defaultDarkBackground = appearanceState.defaultDarkBackground,
            ).let { appearance ->
                emit(appearance)
            }
        }.map { appearance ->
            applyAppearanceSettings(appearance = appearance).single()
        }.flowOn(dispatcher)
            .onCompletion {
                monsterEventDispatcher.dispatchEvent(MonsterEvent.OnCompendiumChanges)
            }
            .launchIn(scope)
    }

    override fun onAppearanceChange(appearance: AppearanceSettingsState) {
        setState { copy(appearanceState = appearance) }
    }

    override fun onImport() {
        shareContentEventDispatcher.dispatchEvent(ShareContentEvent.Import.OnStart)
    }

    override fun onExportContent() {
        shareContentEventDispatcher.dispatchEvent(ShareContentEvent.Export.OnStart())
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

    private fun fillAppearanceSettingsState() {
        getAppearanceSettings()
            .flowOn(dispatcher)
            .map { appearanceSettings ->
                state.value.copy(
                    appearanceState = AppearanceSettingsState(
                        forceLightImageBackground = appearanceSettings.forceLightImageBackground,
                        defaultLightBackground = appearanceSettings.defaultLightBackground,
                        defaultDarkBackground = appearanceSettings.defaultDarkBackground
                    )
                )
            }
            .onEach { state ->
                setState { state }
            }
            .launchIn(scope)
    }

    private fun closeAdvancedSettings() {
        setState { copy(advancedSettingsOpened = false) }
    }

    private fun Language.asState(): SettingsLanguageState {
        val string = when (this) {
            Language.ENGLISH -> "English (United States)"
            Language.PORTUGUESE -> "Português (Brasil)"
        }

        return SettingsLanguageState(
            code = this.code,
            value = string
        )
    }
}
