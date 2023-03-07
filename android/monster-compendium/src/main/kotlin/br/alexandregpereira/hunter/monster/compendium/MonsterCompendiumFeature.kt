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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.monster.compendium.MonsterCompendiumViewAction.GoToCompendiumIndex
import br.alexandregpereira.hunter.monster.compendium.ui.MonsterCompendiumScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun MonsterCompendiumFeature(
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val viewModel: MonsterCompendiumViewModel = koinViewModel()
    var compendiumIndex by remember {
        mutableStateOf(-1)
    }
    MonsterCompendiumScreen(
        state = viewModel.state.collectAsState().value,
        initialScrollItemPosition = viewModel.initialScrollItemPosition,
        compendiumIndex = compendiumIndex,
        contentPadding = contentPadding,
        events = viewModel,
    )

    LaunchedEffect(key1 = Unit) {
        viewModel.action.collect { action ->
            when (action) {
                is GoToCompendiumIndex -> compendiumIndex = action.index
            }
        }
    }
}
