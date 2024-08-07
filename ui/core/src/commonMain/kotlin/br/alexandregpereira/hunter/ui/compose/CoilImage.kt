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

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import br.alexandregpereira.hunter.ui.util.toColor
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
    backgroundColor: String,
    graphicsLayerBlock: GraphicsLayerScope.() -> Unit = {},
) = CoilImage(
    imageUrl = imageUrl,
    contentDescription = contentDescription,
    modifier = modifier,
    shape = shape,
    backgroundColor = remember(backgroundColor) { backgroundColor.toColor() },
    graphicsLayerBlock = graphicsLayerBlock
)

@Composable
fun CoilImage(
    imageUrl: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    backgroundColor: Color? = null,
    graphicsLayerBlock: GraphicsLayerScope.() -> Unit = {},
) = AsyncImage(
    model = ImageRequest.Builder(LocalPlatformContext.current)
        .data(data = imageUrl)
        .crossfade(durationMillis = 300)
        .build(),
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
