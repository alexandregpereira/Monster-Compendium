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

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.domain.model.SpeedType
import br.alexandregpereira.hunter.monster.detail.SpeedState
import br.alexandregpereira.hunter.monster.detail.SpeedValueState
import br.alexandregpereira.hunter.ui.compose.Window
import br.alexandregpereira.hunter.ui.theme.HunterTheme

@Composable
internal fun SpeedBlock(
    speed: SpeedState,
    modifier: Modifier = Modifier,
    contentPaddingBottom: Dp = 0.dp,
) {
    val prefixTitle = strings.speedTitle
    val title = if (speed.hover) {
        "$prefixTitle (${strings.speedHover})"
    } else prefixTitle
    Block(title = title, modifier = modifier, contentPaddingBottom = contentPaddingBottom) {

        SpeedGrid(speed)
    }
}

@Composable
private fun SpeedGrid(
    speed: SpeedState,
) = Grid {

    speed.values.forEach { speedValue ->
        val iconRes = speedValue.type.toIcon()
        IconInfo(title = speedValue.valueFormatted, painter = painterResource(iconRes))
    }
}

@Preview
@Composable
private fun SpeedGridPreview() = Window {
    SpeedGrid(
        speed = SpeedState(
            hover = false, values = (0..6).map {
                SpeedValueState(
                    type = SpeedType.WALK,
                    valueFormatted = "10m"
                )
            }
        )
    )
}

@Preview
@Composable
private fun SpeedBlockPreview() = HunterTheme {
    SpeedBlock(
        speed = SpeedState(
            hover = true, values = listOf(
                SpeedValueState(
                    type = SpeedType.WALK,
                    valueFormatted = "10m"
                )
            )
        )
    )
}
