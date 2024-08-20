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
    getScrollOffset: () -> Int = { 0 },
    onClosed: () -> Unit,
) {
    BackHandler(enabled = isOpen, onBack = onClosed)

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
