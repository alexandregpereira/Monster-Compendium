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

import br.alexandregpereira.hunter.ui.compose.AppImageContentScale

internal data class SettingsViewState(
    val imageBaseUrl: String = "",
    val alternativeSourceBaseUrl: String = "",
    val saveButtonEnabled: Boolean = true,
    val advancedSettingsOpened: Boolean = false,
    val settingsOpened: Boolean = false,
    val appearanceSettingsOpened: Boolean = false,
    val settingsState: SettingsState = SettingsState(),
    val appearanceState: AppearanceSettingsState = AppearanceSettingsState(),
    val strings: SettingsStrings = SettingsEmptyStrings(),
)

internal data class SettingsState(
    val language: SettingsLanguageState = SettingsLanguageState(),
    val languages: List<SettingsLanguageState> = emptyList(),
)

internal data class SettingsLanguageState(
    val code: String = "",
    val value: String = "",
)

internal data class AppearanceSettingsState(
    val forceLightImageBackground: Boolean = false,
    val defaultLightBackground: String = "",
    val defaultDarkBackground: String = "",
    val monsterImageContentScaleOptions: List<AppImageContentScale> = AppImageContentScale.entries,
    val monsterImageContentSelectedOptionIndex: Int = 0,
) {
    val defaultDarkBackgroundEnabled: Boolean = forceLightImageBackground.not()
    val monsterImageContentSelected: AppImageContentScale
        get() = monsterImageContentScaleOptions[monsterImageContentSelectedOptionIndex]
}
