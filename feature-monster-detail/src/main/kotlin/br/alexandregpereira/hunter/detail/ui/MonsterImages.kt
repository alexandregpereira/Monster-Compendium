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

package br.alexandregpereira.hunter.detail.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import br.alexandregpereira.hunter.ui.compose.MonsterCoilImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import kotlin.math.absoluteValue

data class ImageState(val url: String, val contentDescription: String)

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MonsterImages(
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
                1f - calculateCurrentOffsetForPage(pagePosition).absoluteValue.coerceIn(0f, 1f)

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