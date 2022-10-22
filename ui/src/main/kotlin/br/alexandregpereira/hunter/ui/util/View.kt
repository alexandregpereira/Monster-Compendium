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
