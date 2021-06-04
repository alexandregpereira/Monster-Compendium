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

package br.alexandregpereira.hunter.detail.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.util.lerp
import br.alexandregpereira.hunter.ui.compose.MonsterCoilImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import kotlin.math.absoluteValue

data class Image(val url: String, val contentDescription: String)

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MonsterImages(
    images: List<Image>,
    pagerState: PagerState,
    height: Dp,
    shape: Shape,
) = HorizontalPager(
    state = pagerState,
    verticalAlignment = Alignment.Top
) { pagePosition ->
    val image = images[pagePosition]
    MonsterCoilImage(
        imageUrl = image.url,
        contentDescription = image.contentDescription,
        height = height,
        shape = shape,
        graphicsLayerBlock = {
            val fraction =
                1f - calculateCurrentOffsetForPage(pagePosition).absoluteValue.coerceIn(0f, 1f)

            lerp(
                start = 0.4f,
                stop = 1f,
                fraction = fraction
            ).also { scale ->
                scaleX = scale
                scaleY = scale
            }

            alpha = lerp(
                start = 0.2f,
                stop = 1f,
                fraction = fraction
            )
        }
    )
}