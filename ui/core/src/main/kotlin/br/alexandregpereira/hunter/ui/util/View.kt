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

val BottomNavigationHeight = 56.dp
