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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.ui.compose.MonsterCoilImage
import br.alexandregpereira.hunter.ui.compose.monsterAspectRatio
import br.alexandregpereira.hunter.ui.transition.AlphaTransition

data class ImageState(val url: String, val contentDescription: String)

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun MonsterImages(
    images: List<ImageState>,
    pagerState: PagerState,
    shape: Shape,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) = AlphaTransition(
    dataList = images,
    pagerState = pagerState
) { image ->
    MonsterCoilImage(
        imageUrl = image.url,
        contentDescription = image.contentDescription,
        shape = shape,
        modifier = Modifier.monsterAspectRatio().padding(contentPadding)
    )
}
