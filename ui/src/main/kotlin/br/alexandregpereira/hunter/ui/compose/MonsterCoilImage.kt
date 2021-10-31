/*
 * Hunter - DnD 5th edition monster compendium application
 * Copyright (C) 2021 Alexandre Gomes Pereira
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

package br.alexandregpereira.hunter.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import br.alexandregpereira.hunter.ui.util.toColor
import coil.compose.rememberImagePainter

@Composable
fun MonsterCoilImage(
    imageUrl: String,
    contentDescription: String,
    height: Dp,
    shape: Shape,
    modifier: Modifier = Modifier,
    backgroundColor: String? = null,
    graphicsLayerBlock: GraphicsLayerScope.() -> Unit = {},
) {
    Image(
        painter = rememberImagePainter (
            data = imageUrl,
            builder = {
                crossfade(true)
            }
        ),
        contentDescription = contentDescription,
        modifier = modifier
            .height(height)
            .fillMaxWidth()
            .run {
                backgroundColor?.let {
                    background(
                        color = it.toColor(),
                        shape = shape
                    )
                } ?: this
            }
            .graphicsLayer(graphicsLayerBlock)
    )
}
