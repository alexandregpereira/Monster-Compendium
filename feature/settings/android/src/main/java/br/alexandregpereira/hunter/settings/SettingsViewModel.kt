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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.alexandregpereira.hunter.domain.settings.GetAlternativeSourceJsonUrlUseCase
import br.alexandregpereira.hunter.domain.settings.GetMonsterImageJsonUrlUseCase
import br.alexandregpereira.hunter.domain.settings.SaveUrlsUseCase
import br.alexandregpereira.hunter.event.EventDispatcher
import br.alexandregpereira.hunter.event.systembar.BottomBarEvent
import br.alexandregpereira.hunter.event.systembar.dispatchRemoveTopContentEvent
import br.alexandregpereira.hunter.event.systembar.dispatchAddTopContentEvent
import br.alexandregpereira.hunter.monster.content.event.MonsterContentManagerEvent.Show
import br.alexandregpereira.hunter.monster.content.event.MonsterContentManagerEventDispatcher
import br.alexandregpereira.hunter.sync.event.SyncEventDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.zip

internal class SettingsViewModel(
    private val getMonsterImageJsonUrl: GetMonsterImageJsonUrlUseCase,
    private val getAlternativeSourceJsonUrl: GetAlternativeSourceJsonUrlUseCase,
    private val saveUrls: SaveUrlsUseCase,
    private val monsterContentManagerEventDispatcher: MonsterContentManagerEventDispatcher,
    private val dispatcher: CoroutineDispatcher,
    private val syncEventDispatcher: SyncEventDispatcher,
    private val analytics: SettingsAnalytics,
    private val bottomBarEventDispatcher: EventDispatcher<BottomBarEvent>,
) : ViewModel(), SettingsViewIntent {

    private val _state = MutableStateFlow(SettingsViewState())
    val state: StateFlow<SettingsViewState> = _state

    init {
        load()
    }

    override fun onImageBaseUrlChange(value: String) {
        _state.value = state.value.copy(imageBaseUrl = value)
    }

    override fun onAlternativeSourceBaseUrlChange(value: String) {
        _state.value = state.value.copy(alternativeSourceBaseUrl = value)
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
            .launchIn(viewModelScope)
    }

    override fun onManageMonsterContentClick() {
        analytics.trackManageMonsterContentClick()
        monsterContentManagerEventDispatcher.dispatchEvent(Show)
    }

    override fun onAdvancedSettingsClick() {
        _state.value = state.value.copy(advancedSettingsOpened = true)
        bottomBarEventDispatcher.dispatchAddTopContentEvent(topContentId = ADVANCED_SETTINGS_CONTENT)
    }

    override fun onAdvancedSettingsCloseClick() = closeAdvancedSettings()

    private fun load() {
        getMonsterImageJsonUrl()
            .zip(getAlternativeSourceJsonUrl()) { imageBaseUrl, alternativeSourceBaseUrl ->
                state.value.copy(
                    imageBaseUrl = imageBaseUrl,
                    alternativeSourceBaseUrl = alternativeSourceBaseUrl
                )
            }
            .flowOn(dispatcher)
            .catch {
                analytics.logException(it)
            }
            .onEach { state ->
                analytics.trackLoadSettings(state)
                _state.value = state
            }
            .launchIn(viewModelScope)
    }

    private fun closeAdvancedSettings() {
        _state.value = state.value.copy(advancedSettingsOpened = false)
        bottomBarEventDispatcher.dispatchRemoveTopContentEvent(topContentId = ADVANCED_SETTINGS_CONTENT)
    }

    private companion object {
        private const val ADVANCED_SETTINGS_CONTENT = "AdvancedSettings"
    }
}
