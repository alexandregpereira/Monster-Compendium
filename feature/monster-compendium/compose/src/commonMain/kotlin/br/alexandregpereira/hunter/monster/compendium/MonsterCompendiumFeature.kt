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
import br.alexandregpereira.hunter.monster.compendium.state.MonsterCompendiumStateHolder
import br.alexandregpereira.hunter.monster.compendium.state.di.StateRecoveryQualifier
import br.alexandregpereira.hunter.monster.compendium.ui.MonsterCompendiumScreen
import br.alexandregpereira.hunter.ui.compose.StateRecoveryLaunchedEffect
import org.koin.compose.koinInject
import org.koin.core.qualifier.named

@Composable
fun MonsterCompendiumFeature(
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    StateRecoveryLaunchedEffect(
        key = StateRecoveryQualifier,
        stateRecovery = koinInject(named(StateRecoveryQualifier)),
    )

    val stateHolder: MonsterCompendiumStateHolder = koinInject()

    MonsterCompendiumScreen(
        state = stateHolder.state.collectAsState().value,
        actionHandler = stateHolder,
        initialScrollItemPosition = stateHolder.initialScrollItemPosition,
        contentPadding = contentPadding,
        events = stateHolder,
    )
}
