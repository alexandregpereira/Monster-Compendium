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

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import br.alexandregpereira.hunter.ui.util.toColor

@Composable
fun MonsterCoilImage(
    imageUrl: String,
    contentDescription: String,
    shape: Shape = RectangleShape,
    modifier: Modifier = Modifier,
    backgroundColor: String,
    contentScale: ContentScale = ContentScale.Fit,
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
    contentScale: ContentScale = ContentScale.Fit,
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
