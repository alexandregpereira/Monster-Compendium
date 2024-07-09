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

package br.alexandregpereira.hunter.monster.content.preview

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import br.alexandregpereira.hunter.monster.content.preview.di.MonsterContentPreviewStateRecoveryQualifier
import br.alexandregpereira.hunter.monster.content.preview.ui.MonsterContentPreviewScreen
import br.alexandregpereira.hunter.ui.compose.StateRecoveryLaunchedEffect
import org.koin.compose.koinInject
import org.koin.core.qualifier.named

@Composable
internal fun MonsterContentPreviewFeature(
    contentPadding: PaddingValues = PaddingValues(),
) {
    StateRecoveryLaunchedEffect(
        key = MonsterContentPreviewStateRecoveryQualifier,
        stateRecovery = koinInject(named(MonsterContentPreviewStateRecoveryQualifier)),
    )
    val viewModel: MonsterContentPreviewStateHolder = koinInject()
    MonsterContentPreviewScreen(
        state = viewModel.state.collectAsState().value,
        actionHandler = viewModel,
        contentPadding = contentPadding,
        onClose = viewModel::onClose,
        onTableContentOpenButtonClick = viewModel::onTableContentOpenButtonClick,
        onTableContentClose = viewModel::onTableContentClose,
        onTableContentClick = viewModel::onTableContentClick,
        onFirstVisibleItemChange = viewModel::onFirstVisibleItemChange,
    )
}
