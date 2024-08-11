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

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CircleImage(
    imageUrl: String,
    backgroundColor: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
) {
    MonsterCoilImage(
        imageUrl = imageUrl,
        contentDescription = contentDescription,
        shape = CircleShape,
        backgroundColor = backgroundColor,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(size)
            .animatePressed(
                pressedScale = 0.8f,
                onClick = onClick,
                onLongClick = onLongClick
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.surface,
                shape = CircleShape
            )
    )
}
