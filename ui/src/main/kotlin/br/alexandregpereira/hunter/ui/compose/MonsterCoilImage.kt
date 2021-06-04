/*
 * Copyright (c) 2021 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
import com.google.accompanist.coil.rememberCoilPainter

@Composable
fun MonsterCoilImage(
    imageUrl: String,
    contentDescription: String,
    height: Dp,
    shape: Shape,
    backgroundColor: String? = null,
    graphicsLayerBlock: GraphicsLayerScope.() -> Unit = {},
) {
    Image(
        painter = rememberCoilPainter(
            request = imageUrl,
            fadeIn = true,
        ),
        contentDescription = contentDescription,
        modifier = Modifier
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