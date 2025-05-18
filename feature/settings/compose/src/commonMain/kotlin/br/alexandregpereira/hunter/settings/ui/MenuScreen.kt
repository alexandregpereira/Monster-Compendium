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

package br.alexandregpereira.hunter.settings.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.settings.SettingsViewIntent
import br.alexandregpereira.hunter.settings.SettingsViewState

@Composable
internal fun MenuScreen(
    state: SettingsViewState,
    versionName: String,
    contentPadding: PaddingValues = PaddingValues(),
    viewIntent: SettingsViewIntent,
) {
    val strings = state.strings
    Box(
        modifier = Modifier.fillMaxSize().padding(contentPadding)
    ) {
        Column(Modifier.verticalScroll(rememberScrollState())) {
            MenuItem(
                text = strings.openGitHubProject,
                onClick = viewIntent::onOpenGitHubProjectClick
            )

            Divider()

            MenuItem(
                text = strings.settingsTitle,
                onClick = viewIntent::onSettingsClick
            )

            Divider()

            MenuItem(
                text = strings.manageAdvancedSettings,
                onClick = viewIntent::onAdvancedSettingsClick
            )

            Divider()

            MenuItem(
                text = strings.appearanceSettingsTitle,
                onClick = viewIntent::onAppearanceSettingsClick
            )

            Divider()

            MenuItem(
                text = strings.importContent,
                onClick = viewIntent::onImport
            )

            Divider()

            MenuItem(
                text = strings.manageMonsterContent,
                onClick = viewIntent::onManageMonsterContentClick
            )

            Divider()

            MenuItem(
                text = strings.donateStrings.buyMeACoffee,
                onClick = viewIntent::onDonateClick
            )
        }

        if (versionName.isNotBlank()) {
            Text(
                text = "v$versionName",
                fontWeight = FontWeight.Light,
                fontSize = 12.sp,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
            )
        }
    }
}

@Composable
private fun MenuItem(
    text: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Normal,
            fontSize = 22.sp,
            modifier = Modifier
                .padding(16.dp)
        )
    }
}
