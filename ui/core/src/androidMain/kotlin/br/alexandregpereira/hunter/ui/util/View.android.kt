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

package br.alexandregpereira.hunter.ui.util

import android.content.Context
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

fun Context.createComposeView(
    withBottomBar: Boolean = false,
    content: @Composable (PaddingValues) -> Unit
): ComposeView {
    return ComposeView(this).apply {
        setContentWithPadding(withBottomBar, content)
    }
}

fun ComposeView.setContentWithPadding(
    withBottomBar: Boolean = false,
    content: @Composable (PaddingValues) -> Unit
) {
    setContent {
        val bottomBarNavigationSize = if (withBottomBar) BottomNavigationHeight else 0.dp
        val insets = WindowInsets.systemBars
        val density = LocalDensity.current
        val top = with(density) { insets.getTop(this).toDp() }
        val bottom = with(density) { insets.getBottom(this).toDp() }
        content(
            PaddingValues(
                top = top,
                bottom = bottom + bottomBarNavigationSize
            )
        )
    }
}
