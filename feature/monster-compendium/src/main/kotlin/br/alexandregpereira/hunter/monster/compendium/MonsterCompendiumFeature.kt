/*
 * Copyright (C) 2022 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.monster.compendium

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.alexandregpereira.hunter.monster.compendium.ui.MonsterCompendiumScreen

@Composable
fun MonsterCompendiumFeature(
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val viewModel: MonsterCompendiumViewModel = viewModel()
    MonsterCompendiumScreen(
        state = viewModel.state.collectAsState().value,
        contentPadding = contentPadding,
        events = viewModel,
    )
}
