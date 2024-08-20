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

package br.alexandregpereira.hunter.ui.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PopupContainer(
    modifier: Modifier = Modifier,
    isOpened: Boolean = false,
    onPopupClosed: () -> Unit = {},
    content: @Composable BoxScope.() -> Unit = {},
    popupContent: @Composable BoxScope.() -> Unit = {},
) {
    Box(modifier.fillMaxSize()) {
        content()

        Closeable(isOpen = isOpened, onClosed = onPopupClosed)

        Box(
            Modifier
                .align(Alignment.BottomEnd)
                .padding(horizontal = 14.dp)
        ) {
            popupContent()
        }
    }
}
