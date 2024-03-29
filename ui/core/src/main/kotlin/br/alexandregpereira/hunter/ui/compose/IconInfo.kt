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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.ui.R
import br.alexandregpereira.hunter.ui.theme.HunterTheme

@Composable
fun IconInfo(
    painter: Painter,
    modifier: Modifier = Modifier,
    iconSize: Dp = 56.dp,
    iconColor: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
    iconAlpha: Float = 0.7f,
    title: String? = null,
    iconText: String? = null,
    iconTextPadding: PaddingValues = PaddingValues(0.dp)
) = Column(
    modifier,
    horizontalAlignment = Alignment.CenterHorizontally
) {

    Box(contentAlignment = Alignment.Center) {
        Icon(
            painter = painter,
            contentDescription = title,
            tint = iconColor,
            modifier = Modifier
                .size(iconSize)
                .alpha(iconAlpha)
        )

        iconText?.let {
            Text(
                text = it,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.White,
                modifier = Modifier.padding(iconTextPadding)
            )
        }
    }

    title?.let {
        Text(
            text = it,
            fontWeight = FontWeight.Light,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .width(72.dp)
                .padding(top = 8.dp)
        )
    }
}

@Preview
@Composable
fun IconInfoArmorClassPreview() {
    HunterTheme {
        Surface {
            IconInfo(
                painter = painterResource(id = R.drawable.ic_school_conjuration),
                iconColor = Color.Blue,
                iconText = "10",
                iconAlpha = 1f
            )
        }
    }
}

@Preview
@Composable
fun IconInfoHitPointPreview() {
    HunterTheme {
        Surface {
            IconInfo(
                title = "28d20 + 252",
                painter = rememberVectorPainter(image = Icons.Filled.Favorite),
                iconColor = Color.Red,
                iconText = "100",
                iconAlpha = 1f,
                iconTextPadding = PaddingValues(bottom = 4.dp)
            )
        }
    }
}
