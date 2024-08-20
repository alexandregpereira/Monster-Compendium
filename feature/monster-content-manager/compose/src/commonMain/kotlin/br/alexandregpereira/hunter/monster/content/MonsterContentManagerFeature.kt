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

package br.alexandregpereira.hunter.monster.content

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import br.alexandregpereira.hunter.monster.content.di.MonsterContentManagerStateRecoveryQualifier
import br.alexandregpereira.hunter.monster.content.preview.MonsterContentPreviewFeature
import br.alexandregpereira.hunter.monster.content.ui.MonsterContentManagerScreen
import br.alexandregpereira.hunter.ui.compose.StateRecoveryLaunchedEffect
import org.koin.compose.koinInject
import org.koin.core.qualifier.named

@Composable
fun MonsterContentManagerFeature(
    contentPadding: PaddingValues = PaddingValues(),
) {
    StateRecoveryLaunchedEffect(
        key = MonsterContentManagerStateRecoveryQualifier,
        stateRecovery = koinInject(named(MonsterContentManagerStateRecoveryQualifier)),
    )
    val viewModel: MonsterContentManagerStateHolder = koinInject()
    MonsterContentManagerScreen(
        state = viewModel.state.collectAsState().value,
        contentPadding = contentPadding,
        onClose = viewModel::onClose,
        onAddClick = viewModel::onAddContentClick,
        onRemoveClick = viewModel::onRemoveContentClick,
        onPreviewClick = viewModel::onPreviewClick,
    )

    MonsterContentPreviewFeature(contentPadding)
}
