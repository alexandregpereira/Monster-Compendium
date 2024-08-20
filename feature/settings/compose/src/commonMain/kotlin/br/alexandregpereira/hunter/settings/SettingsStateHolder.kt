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

package br.alexandregpereira.hunter.settings

import br.alexadregpereira.hunter.shareContent.event.ShareContentEvent
import br.alexadregpereira.hunter.shareContent.event.ShareContentEventDispatcher
import br.alexandregpereira.hunter.domain.settings.AppSettingsImageContentScale
import br.alexandregpereira.hunter.domain.settings.AppearanceSettings
import br.alexandregpereira.hunter.domain.settings.GetAlternativeSourceJsonUrlUseCase
import br.alexandregpereira.hunter.domain.settings.GetMonsterImageJsonUrlUseCase
import br.alexandregpereira.hunter.domain.settings.SaveLanguageUseCase
import br.alexandregpereira.hunter.domain.settings.SaveUrlsUseCase
import br.alexandregpereira.hunter.localization.AppReactiveLocalization
import br.alexandregpereira.hunter.localization.Language
import br.alexandregpereira.hunter.monster.content.event.MonsterContentManagerEvent.Show
import br.alexandregpereira.hunter.monster.content.event.MonsterContentManagerEventDispatcher
import br.alexandregpereira.hunter.monster.event.MonsterEvent
import br.alexandregpereira.hunter.monster.event.MonsterEventDispatcher
import br.alexandregpereira.hunter.settings.domain.ApplyAppearanceSettings
import br.alexandregpereira.hunter.settings.domain.GetAppearanceSettingsFromMonsters
import br.alexandregpereira.hunter.state.MutableActionHandler
import br.alexandregpereira.hunter.state.UiModel
import br.alexandregpereira.hunter.sync.event.SyncEventDispatcher
import br.alexandregpereira.hunter.ui.compose.AppImageContentScale
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
    private val appLocalization: AppReactiveLocalization,
    private val saveLanguage: SaveLanguageUseCase,
    private val getAppearanceSettings: GetAppearanceSettingsFromMonsters,
    private val applyAppearanceSettings: ApplyAppearanceSettings,
) : UiModel<SettingsViewState>(SettingsViewState()), SettingsViewIntent,
    MutableActionHandler<SettingsViewAction> by MutableActionHandler() {

    private val strings: SettingsStrings
        get() = getSettingsStrings(appLocalization.getLanguage())
    private var originalSettingsState: SettingsState = SettingsState()

    init {
        observeLanguageChanges()
        load()
    }

    private fun observeLanguageChanges() {
        appLocalization.languageFlow.onEach {
            load()
        }.launchIn(scope)
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
        analytics.trackAppearanceSettingsClick()
        fillAppearanceSettingsState()
        setState { copy(appearanceSettingsOpened = true) }
    }

    override fun onAppearanceSettingsCloseClick() {
        setState { copy(appearanceSettingsOpened = false) }
    }

    override fun onAppearanceSettingsSaveClick() {
        analytics.trackAppearanceSettingsSaveClick()
        onAppearanceSettingsCloseClick()
        val appearanceState = state.value.appearanceState
        flow {
            AppearanceSettings(
                forceLightImageBackground = appearanceState.forceLightImageBackground,
                defaultLightBackground = appearanceState.defaultLightBackground,
                defaultDarkBackground = appearanceState.defaultDarkBackground,
                imageContentScale = when (appearanceState.monsterImageContentSelected) {
                    AppImageContentScale.Fit -> AppSettingsImageContentScale.Fit
                    AppImageContentScale.Crop -> AppSettingsImageContentScale.Crop
                }
            ).let { appearance ->
                emit(appearance)
            }
        }.map { appearance ->
            applyAppearanceSettings(appearance = appearance).single()
        }.flowOn(dispatcher)
            .onCompletion {
                monsterEventDispatcher.dispatchEvent(MonsterEvent.OnCompendiumChanges())
            }
            .launchIn(scope)
    }

    override fun onAppearanceChange(appearance: AppearanceSettingsState) {
        setState { copy(appearanceState = appearance) }
    }

    override fun onImport() {
        analytics.trackImportContentClick()
        shareContentEventDispatcher.dispatchEvent(ShareContentEvent.Import.OnStart)
    }

    override fun onOpenGitHubProjectClick() {
        analytics.trackOpenGitHubProjectClick()
        SettingsViewAction.GoToExternalUrl(
            url = "https://github.com/alexandregpereira/Monster-Compendium"
        ).also { sendAction(it) }
    }

    override fun onDonateClick() {
        analytics.trackDonateClick()
        setState { copy(donateIsOpen = true) }
    }

    fun onDonateCloseClick() {
        setState { copy(donateIsOpen = false) }
    }

    fun onPixCodeCopyClick() {
        analytics.trackPixCodeCopyClick()
    }

    fun onPixKeyCopyClick() {
        analytics.trackPixKeyCopyClick()
    }

    fun onBuyMeCoffeeClick() {
        analytics.trackBuyMeCoffeeClick()
        SettingsViewAction.GoToExternalUrl(
            url = "https://ko-fi.com/monstercompendium"
        ).also { sendAction(it) }
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
                        defaultDarkBackground = appearanceSettings.defaultDarkBackground,
                        monsterImageContentSelectedOptionIndex = appearanceSettings.imageContentScale.ordinal
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
