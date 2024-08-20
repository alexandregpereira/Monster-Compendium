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

package br.alexandregpereira.hunter.monster.registration

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import br.alexandregpereira.hunter.monster.registration.ui.MonsterRegistrationScreen
import org.koin.compose.koinInject

@Composable
fun MonsterRegistrationFeature(
    contentPadding: PaddingValues = PaddingValues(),
) {
    val viewModel: MonsterRegistrationStateHolder = koinInject()
    MonsterRegistrationScreen(
        state = viewModel.state.collectAsState().value,
        actionHandler = viewModel,
        contentPadding = contentPadding,
        intent = viewModel,
    )
}
