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
