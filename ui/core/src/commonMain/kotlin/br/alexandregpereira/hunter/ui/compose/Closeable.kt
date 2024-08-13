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

package br.alexandregpereira.hunter.ui.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.util.lerp
import kotlin.math.absoluteValue

@Composable
fun Closeable(
    isOpen: Boolean,
    modifier: Modifier = Modifier,
    backHandlerEnabled: Boolean = isOpen,
    getScrollOffset: () -> Int = { 0 },
    onClosed: () -> Unit,
) {
    BackHandler(enabled = backHandlerEnabled, onBack = onClosed)

    AnimatedVisibility(
        modifier = modifier,
        visible = isOpen,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        val offset = getScrollOffset().absoluteValue
        val fraction = offset.coerceAtMost(300) / 300f
        val alpha = lerp(
            start = .7f,
            stop = .3f,
            fraction = fraction
        )
        Box(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background.copy(alpha = alpha))
                .noIndicationClick(onClick = onClosed)
        )
    }
}
