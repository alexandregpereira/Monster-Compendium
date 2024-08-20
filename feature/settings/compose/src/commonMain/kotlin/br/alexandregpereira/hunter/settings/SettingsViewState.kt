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
    val donateState: DonateState = DonateState(),
)

internal data class DonateState(
    val coverImageUrl: String = "https://raw.githubusercontent.com/alexandregpereira/Monster-Compendium/main/content/media/buy-me-coffee-cover.png",
    val pixCode: String = "00020126580014BR.GOV.BCB.PIX01365bc29fc7-557c-4935-bdad-1d1f53dd29e65204000053039865802BR5923Alexandre Gomes Pereira6009SAO PAULO62140510FdKlqycExz6304BCEF",
    val pixKey: String = "5bc29fc7-557c-4935-bdad-1d1f53dd29e6",
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
