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
import br.alexandregpereira.hunter.event.v2.EventDispatcher
import br.alexandregpereira.hunter.event.v2.EventListener
import br.alexandregpereira.hunter.localization.AppReactiveLocalization
import br.alexandregpereira.hunter.localization.Language
import br.alexandregpereira.hunter.monster.content.event.MonsterContentManagerEvent.Show
import br.alexandregpereira.hunter.monster.content.event.MonsterContentManagerEventDispatcher
import br.alexandregpereira.hunter.monster.event.MonsterEvent
import br.alexandregpereira.hunter.monster.event.MonsterEventDispatcher
import br.alexandregpereira.hunter.paywall.event.PaywallEvent
import br.alexandregpereira.hunter.paywall.event.PaywallResult
import br.alexandregpereira.hunter.revenue.IsSessionUsageLimitReached
import br.alexandregpereira.hunter.settings.domain.ApplyAppearanceSettings
import br.alexandregpereira.hunter.settings.domain.GetAppearanceSettingsFromMonsters
import br.alexandregpereira.hunter.settings.domain.IsManageContentFeatureEnabled
import br.alexandregpereira.hunter.spell.compendium.event.SpellCompendiumEvent
import br.alexandregpereira.hunter.spell.compendium.event.SpellCompendiumEventResultDispatcher
import br.alexandregpereira.hunter.spell.compendium.event.SpellCompendiumResult
import br.alexandregpereira.hunter.spell.detail.event.SpellDetailEvent
import br.alexandregpereira.hunter.spell.detail.event.SpellDetailEventDispatcher
import br.alexandregpereira.hunter.spell.registration.event.SpellRegistrationEvent
import br.alexandregpereira.hunter.state.MutableActionHandler
import br.alexandregpereira.hunter.state.UiModel
import br.alexandregpereira.hunter.sync.event.SyncEventDispatcher
import br.alexandregpereira.hunter.ui.compose.AppImageContentScale
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.single

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
    private val paywallEventDispatcher: EventDispatcher<PaywallEvent>,
    private val isSessionUsageLimitReached: IsSessionUsageLimitReached,
    private val paywallResultListener: EventListener<PaywallResult>,
    private val spellCompendiumEventDispatcher: SpellCompendiumEventResultDispatcher,
    private val spellDetailEventDispatcher: SpellDetailEventDispatcher,
    private val spellRegistrationEventDispatcher: EventDispatcher<SpellRegistrationEvent>,
    private val isManageContentFeatureEnabled: IsManageContentFeatureEnabled,
) : UiModel<SettingsViewState>(SettingsViewState()), SettingsViewIntent,
    MutableActionHandler<SettingsViewAction> by MutableActionHandler() {

    private val strings: SettingsStrings
        get() = getSettingsStrings(appLocalization.getLanguage())
    private var originalSettingsState: SettingsState = SettingsState()

    private fun observeLanguageChanges() {
        appLocalization.languageFlow.onEach {
            load()
        }.launchIn(scope)

        paywallResultListener.events.onEach { result ->
            if (result is PaywallResult.OnSubscribe) {
                load()
            }
        }.launchIn(scope)
    }

    fun onStart() {
        observeLanguageChanges()
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

    private fun onManageMonsterContentClick() {
        analytics.trackManageMonsterContentClick()
        monsterContentManagerEventDispatcher.dispatchEvent(Show)
    }

    private fun onAdvancedSettingsClick() {
        analytics.trackAdvancedSettingsClick()
        setState { copy(advancedSettingsOpened = true) }
    }

    override fun onAdvancedSettingsCloseClick() = closeAdvancedSettings()

    private fun onSettingsClick() {
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

    private fun onAppearanceSettingsClick() {
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

    private fun onImport() {
        analytics.trackImportContentClick()
        shareContentEventDispatcher.dispatchEvent(ShareContentEvent.Import.OnStart)
    }

    private fun onOpenGitHubProjectClick() {
        analytics.trackOpenGitHubProjectClick()
        SettingsViewAction.GoToExternalUrl(
            url = "https://github.com/alexandregpereira/Monster-Compendium"
        ).also { sendAction(it) }
    }

    private fun onSubscribePremiumClick() {
        paywallEventDispatcher.dispatchEvent(PaywallEvent.ShowPaywall)
    }

    private fun onSpellsClick() {
        analytics.trackSpellsClick()
        spellCompendiumEventDispatcher.dispatchEventResult(event = SpellCompendiumEvent.Show())
            .onEach { spellCompendiumResult ->
                when (spellCompendiumResult) {
                    is SpellCompendiumResult.OnSpellClick -> {
                        spellDetailEventDispatcher.dispatchEvent(
                            SpellDetailEvent.ShowSpell(spellCompendiumResult.spellIndex)
                        )
                    }
                    is SpellCompendiumResult.OnSpellLongClick -> {
                        spellRegistrationEventDispatcher.dispatchEvent(
                            SpellRegistrationEvent.Show(spellCompendiumResult.spellIndex)
                        )
                    }
                }
            }
            .launchIn(scope)
    }

    private fun load() {
        val currentState = state.value
        flow {
            coroutineScope {
                val currentStrings = this@SettingsStateHolder.strings
                val newState = currentState.copy(
                    strings = currentStrings,
                    settingsState = SettingsState(
                        languages = Language.entries.map { it.asState() },
                        language = appLocalization.getLanguage().asState()
                    ),
                    menuItems = buildMenuItems(
                        strings = currentStrings,
                        showPremium = false,
                        isManageContentFeatureEnabled = false,
                    ),
                )
                emit(newState)
                val imageBaseUrlDeferred = async { getMonsterImageJsonUrl().single() }
                val alternativeSourceBaseUrlDeferred = async { getAlternativeSourceJsonUrl().single() }
                val isSessionUsageLimitReachedDeferred = async { isSessionUsageLimitReached() }
                val isManageContentFeatureEnabled = async { isManageContentFeatureEnabled() }
                val newState2 = currentState.copy(
                    imageBaseUrl = imageBaseUrlDeferred.await(),
                    alternativeSourceBaseUrl = alternativeSourceBaseUrlDeferred.await(),
                    menuItems = buildMenuItems(
                        strings = currentStrings,
                        showPremium = isSessionUsageLimitReachedDeferred.await(),
                        isManageContentFeatureEnabled = isManageContentFeatureEnabled.await(),
                    ),
                )
                emit(newState2)
            }
        }.flowOn(dispatcher)
            .catch {
                analytics.logException(it)
            }
            .onEach { newState ->
                if (currentState.alternativeSourceBaseUrl != newState.alternativeSourceBaseUrl ||
                    currentState.imageBaseUrl != newState.imageBaseUrl
                ) {
                    analytics.trackLoadSettings(newState)
                }
                originalSettingsState = newState.settingsState
                setState { newState }
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

    override fun onMenuItemClick(id: MenuItemIdState) {
        when (id) {
            MenuItemIdState.OPEN_GITHUB_PROJECT -> onOpenGitHubProjectClick()
            MenuItemIdState.SETTINGS -> onSettingsClick()
            MenuItemIdState.ADVANCED_SETTINGS -> onAdvancedSettingsClick()
            MenuItemIdState.APPEARANCE_SETTINGS -> onAppearanceSettingsClick()
            MenuItemIdState.IMPORT_CONTENT -> onImport()
            MenuItemIdState.SPELLS -> onSpellsClick()
            MenuItemIdState.MANAGE_MONSTER_CONTENT -> onManageMonsterContentClick()
            MenuItemIdState.SUBSCRIBE_PREMIUM -> onSubscribePremiumClick()
        }
    }

    private fun buildMenuItems(
        strings: SettingsStrings,
        showPremium: Boolean,
        isManageContentFeatureEnabled: Boolean,
    ): ImmutableList<MenuItemState> =
        buildList {
            add(MenuItemState(MenuItemIdState.OPEN_GITHUB_PROJECT, strings.openGitHubProject))
            add(MenuItemState(MenuItemIdState.SETTINGS, strings.settingsTitle))
            add(MenuItemState(MenuItemIdState.ADVANCED_SETTINGS, strings.manageAdvancedSettings))
            add(MenuItemState(MenuItemIdState.APPEARANCE_SETTINGS, strings.appearanceSettingsTitle))
            add(MenuItemState(MenuItemIdState.IMPORT_CONTENT, strings.importContent))
            add(MenuItemState(MenuItemIdState.SPELLS, strings.spells))
            if (isManageContentFeatureEnabled) {
                add(MenuItemState(MenuItemIdState.MANAGE_MONSTER_CONTENT, strings.manageMonsterContent))
            }
            if (showPremium) {
                add(MenuItemState(MenuItemIdState.SUBSCRIBE_PREMIUM, strings.subscribePremium))
            }
        }.toImmutableList()

    private fun closeAdvancedSettings() {
        setState { copy(advancedSettingsOpened = false) }
    }

    private fun Language.asState(): SettingsLanguageState {
        val string = when (this) {
            Language.ENGLISH -> "English (United States)"
            Language.PORTUGUESE -> "Português (Brasil)"
            Language.SPANISH -> "Español"
        }

        return SettingsLanguageState(
            code = this.code,
            value = string
        )
    }
}
