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
    val strings: SettingsStrings = SettingsEnStrings(),
    val donateIsOpen: Boolean = false,
    val donateState: DonateState = DonateState(strings = strings.donateStrings),
)

internal data class DonateState(
    val coverImageUrl: String = "https://raw.githubusercontent.com/alexandregpereira/Monster-Compendium/main/content/media/buy-me-coffee-cover.png",
    val pixCode: String = "00020126580014BR.GOV.BCB.PIX01365bc29fc7-557c-4935-bdad-1d1f53dd29e65204000053039865802BR5923Alexandre Gomes Pereira6009SAO PAULO62140510FdKlqycExz6304BCEF",
    val strings: DonateStrings,
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
