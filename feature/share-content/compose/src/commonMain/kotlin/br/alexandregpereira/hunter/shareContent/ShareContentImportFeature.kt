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

package br.alexandregpereira.hunter.shareContent

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.shareContent.state.ShareContentImportStateHolder
import br.alexandregpereira.hunter.shareContent.ui.ShareContentImportScreen
import br.alexandregpereira.hunter.state.compose.rememberStateHolder
import br.alexandregpereira.hunter.ui.compose.BottomSheet

@Composable
fun ShareContentImportFeature(
    contentPadding: PaddingValues = PaddingValues(),
) {
    val stateHolder = rememberStateHolder<ShareContentImportStateHolder>()
    val state by stateHolder.state.collectAsState()
    BottomSheet(
        contentPadding = PaddingValues(
            end = 16.dp,
            start = 16.dp,
            bottom = 16.dp + contentPadding.calculateBottomPadding(),
        ),
        topSpaceHeight = 0.dp,
        opened = state.isOpen,
        onClose = stateHolder::onClose,
        modifier = Modifier.animateContentSize()
    ) {
        ShareContentImportScreen(
            state = state,
            onImport = stateHolder::onImport,
            onFilePicked = stateHolder::onFilePicked,
        )
    }
}
