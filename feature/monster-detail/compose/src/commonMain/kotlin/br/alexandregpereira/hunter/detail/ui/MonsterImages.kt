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

package br.alexandregpereira.hunter.detail.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.ui.compose.AppImageContentScale
import br.alexandregpereira.hunter.ui.compose.MonsterCoilImage
import br.alexandregpereira.hunter.ui.compose.monsterAspectRatio
import br.alexandregpereira.hunter.ui.transition.AlphaTransition

data class ImageState(
    val url: String,
    val contentDescription: String,
    val contentScale: AppImageContentScale,
)

@Composable
internal fun MonsterImages(
    images: List<ImageState>,
    pagerState: PagerState,
    shape: Shape,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) = AlphaTransition(
    dataList = images,
    pagerState = pagerState,
    modifier = modifier,
) { image ->
    MonsterCoilImage(
        imageUrl = image.url,
        contentDescription = image.contentDescription,
        shape = shape,
        contentScale = image.contentScale,
        modifier = Modifier.monsterAspectRatio().padding(contentPadding)
    )
}
