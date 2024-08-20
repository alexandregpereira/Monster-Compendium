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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.detail.ui.resources.Res
import br.alexandregpereira.hunter.detail.ui.resources.ic_hit_point
import br.alexandregpereira.hunter.detail.ui.resources.ic_shield
import br.alexandregpereira.hunter.monster.detail.StatsState
import br.alexandregpereira.hunter.ui.compose.Window
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun StatsBlock(
    stats: StatsState,
    modifier: Modifier = Modifier
) = Block(modifier = modifier) {

    StatsGrid(stats = stats)
}

@Composable
private fun StatsGrid(
    stats: StatsState,
) = Grid {
    IconInfo(
        title = strings.armorClass,
        painter = painterResource(Res.drawable.ic_shield),
        iconSize = 72.dp,
        iconText = stats.armorClass.toString(),
    )

    IconInfo(
        title = stats.hitDice,
        painter = painterResource(Res.drawable.ic_hit_point),
        iconSize = 72.dp,
        iconText = stats.hitPoints.toString(),
        iconPadding = PaddingValues(top = 4.dp),
    )
}

@Preview
@Composable
private fun StatsGridPreview() = Window {
    StatsGrid(
        stats = StatsState(armorClass = 20, hitPoints = 100, hitDice = "28d20 + 252")
    )
}

@Preview
@Composable
private fun StatsBlockPreview() = Window {
    StatsBlock(stats = StatsState(armorClass = 0, hitPoints = 0, hitDice = "teasdas"))
}
