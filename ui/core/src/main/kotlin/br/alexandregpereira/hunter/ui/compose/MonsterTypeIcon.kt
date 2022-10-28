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

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun MonsterTypeIcon(
    @DrawableRes iconRes: Int,
    iconSize: Dp,
    modifier: Modifier = Modifier,
    contentDescription: String = ""
) = Box(
    contentAlignment = Alignment.TopEnd,
    modifier = modifier
        .fillMaxWidth()
        .padding(8.dp),
) {
    Icon(
        painter = painterResource(iconRes),
        contentDescription = contentDescription,
        tint = Color.Black.copy(alpha = LocalContentAlpha.current),
        modifier = Modifier
            .size(iconSize)
            .alpha(0.7f)
    )
}