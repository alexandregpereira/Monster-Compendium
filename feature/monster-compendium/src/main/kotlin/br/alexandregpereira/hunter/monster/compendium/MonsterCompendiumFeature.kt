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
