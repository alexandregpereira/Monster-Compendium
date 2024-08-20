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

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade

@Composable
fun CoilImage(
    imageUrl: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    backgroundColor: Color? = null,
    contentScale: AppImageContentScale,
    graphicsLayerBlock: GraphicsLayerScope.() -> Unit = {},
) = AsyncImage(
    model = ImageRequest.Builder(LocalPlatformContext.current)
        .data(data = imageUrl)
        .crossfade(durationMillis = 300)
        .build(),
    contentScale = when (contentScale) {
        AppImageContentScale.Fit -> ContentScale.Fit
        AppImageContentScale.Crop -> ContentScale.Crop
    },
    contentDescription = contentDescription,
    modifier = modifier
        .run {
            backgroundColor?.let { color ->
                background(
                    color = color,
                    shape = shape
                )
            } ?: this
        }
        .clip(shape = shape)
        .graphicsLayer(graphicsLayerBlock)
)

enum class AppImageContentScale {
    Fit,
    Crop,
}
