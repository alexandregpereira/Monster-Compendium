/*
 * Copyright (C) 2022 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ProvideWindowInsets

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
        val bottomBarNavigationSize = if (withBottomBar) 58.dp else 0.dp
        ProvideWindowInsets {
            val insets = LocalWindowInsets.current
            val top = with(LocalDensity.current) { insets.systemBars.top.toDp() }
            val bottom = with(LocalDensity.current) { insets.systemBars.bottom.toDp() }
            content(
                PaddingValues(
                    top = top,
                    bottom = bottom + bottomBarNavigationSize
                )
            )
        }
    }
}
