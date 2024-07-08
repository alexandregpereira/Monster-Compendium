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

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import br.alexandregpereira.hunter.ui.compose.MonsterCoilImage
import kotlin.math.absoluteValue

data class ImageState(val url: String, val contentDescription: String)

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun MonsterImages(
    images: List<ImageState>,
    pagerState: PagerState,
    height: Dp,
    shape: Shape,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) = HorizontalPager(
    state = pagerState
) { pagePosition ->
    val image = images[pagePosition]
    MonsterCoilImage(
        imageUrl = image.url,
        contentDescription = image.contentDescription,
        height = height,
        shape = shape,
        graphicsLayerBlock = {
            val fraction =
                1f - pagerState.calculateCurrentOffsetForPage(pagePosition).absoluteValue.coerceIn(0f, 1f)

            lerp(
                start = 0.4f,
                stop = 1f,
                fraction = fraction
            ).also { scale ->
                scaleX = scale
                scaleY = scale
            }
        },
        modifier = Modifier.padding(contentPadding)
    )
}

@OptIn(ExperimentalFoundationApi::class)
private fun PagerState.calculateCurrentOffsetForPage(page: Int): Float {
    return (this.currentPage - page) + this.currentPageOffsetFraction
}
