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

package br.alexandregpereira.hunter.detail.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun Grid(
    modifier: Modifier = Modifier,
    content: @Composable FlowRowScope.() -> Unit
) = FlowRow(
    modifier.fillMaxWidth(),
    horizontalArrangement = GridArrangementHorizontal(),
    verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.Top),
    content = content
)

internal val GridItemWidth = 120.dp

private class GridArrangementHorizontal : Arrangement.Horizontal {

    override val spacing: Dp = 8.dp

    override fun Density.arrange(
        totalSize: Int,
        sizes: IntArray,
        layoutDirection: LayoutDirection,
        outPositions: IntArray
    ) {
        val consumedSize = sizes.fold(0) { a, b -> a + b }
        val gapSize = ((totalSize - consumedSize).toFloat() / (sizes.size + 1))
        var current = gapSize
        sizes.forEachIndexed { index, it ->
            outPositions[index] = current.roundToInt()
            current += it.toFloat() + gapSize
        }
    }
}
