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

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import br.alexandregpereira.hunter.ui.util.toColor

@Composable
fun MonsterCoilImage(
    imageUrl: String,
    contentDescription: String,
    shape: Shape = RectangleShape,
    modifier: Modifier = Modifier,
    backgroundColor: String,
    contentScale: AppImageContentScale,
    graphicsLayerBlock: GraphicsLayerScope.() -> Unit = {},
) = MonsterCoilImage(
    imageUrl = imageUrl,
    contentDescription = contentDescription,
    modifier = modifier,
    shape = shape,
    backgroundColor = remember(backgroundColor) { backgroundColor.toColor() },
    contentScale = contentScale,
    graphicsLayerBlock = graphicsLayerBlock
)

@Composable
fun MonsterCoilImage(
    imageUrl: String,
    contentDescription: String,
    shape: Shape = RectangleShape,
    modifier: Modifier = Modifier,
    backgroundColor: Color? = null,
    contentScale: AppImageContentScale,
    graphicsLayerBlock: GraphicsLayerScope.() -> Unit = {},
) {
    CoilImage(
        imageUrl,
        contentDescription,
        modifier.fillMaxSize(),
        shape,
        backgroundColor,
        contentScale,
        graphicsLayerBlock
    )
}
