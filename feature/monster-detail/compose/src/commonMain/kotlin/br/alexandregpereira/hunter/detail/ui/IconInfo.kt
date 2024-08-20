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
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.detail.ui.resources.Res
import br.alexandregpereira.hunter.detail.ui.resources.ic_shield
import br.alexandregpereira.hunter.ui.compose.AppSurface
import br.alexandregpereira.hunter.ui.theme.HunterTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import br.alexandregpereira.hunter.ui.compose.IconInfo as IconInfoUi

@Composable
internal fun IconInfo(
    title: String,
    painter: Painter,
    modifier: Modifier = Modifier,
    iconSize: Dp = 56.dp,
    iconColor: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
    iconAlpha: Float = 0.7f,
    iconText: String? = null,
    iconPadding: PaddingValues = PaddingValues(0.dp)
) = IconInfoUi(
    title = title,
    painter = painter,
    iconColor = iconColor,
    iconSize = iconSize,
    iconAlpha = iconAlpha,
    iconText = iconText,
    iconPadding = iconPadding,
    modifier = modifier
)

@Preview
@Composable
private fun IconInfoArmorClassPreview() {
    HunterTheme {
        AppSurface {
            IconInfo(
                title = "Armor Class",
                painter = painterResource(Res.drawable.ic_shield),
                iconColor = Color.Blue,
                iconText = "10",
                iconAlpha = 1f
            )
        }
    }
}

@Preview
@Composable
private fun IconInfoHitPointPreview() {
    HunterTheme {
        AppSurface {
            IconInfo(
                title = "28d20 + 252",
                painter = rememberVectorPainter(image = Icons.Filled.Favorite),
                iconColor = Color.Red,
                iconText = "100",
                iconAlpha = 1f,
                iconPadding = PaddingValues(bottom = 4.dp)
            )
        }
    }
}
