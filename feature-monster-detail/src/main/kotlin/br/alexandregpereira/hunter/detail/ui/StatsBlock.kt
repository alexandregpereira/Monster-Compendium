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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.detail.R
import br.alexandregpereira.hunter.domain.model.Stats
import br.alexandregpereira.hunter.ui.compose.Window

@Composable
fun StatsBlock(
    stats: Stats,
    modifier: Modifier = Modifier
) = Block(modifier = modifier) {

    StatsGrid(stats = stats)
}

@Composable
fun StatsGrid(
    stats: Stats,
) = Grid {
    IconInfo(
        title = stringResource(R.string.monster_detail_armor_class),
        painter = painterResource(id = R.drawable.ic_shield),
        iconColor = Color.Blue,
        iconText = stats.armorClass.toString(),
        iconAlpha = 1f
    )

    IconInfo(
        title = stats.hitDice,
        painter = rememberVectorPainter(image = Icons.Filled.Favorite),
        iconColor = Color.Red,
        iconText = stats.hitPoints.toString(),
        iconAlpha = 1f,
        iconTextPadding = PaddingValues(bottom = 4.dp)
    )
}

@Preview
@Composable
fun StatsGridPreview() = Window {
    StatsGrid(
        stats = Stats(armorClass = 20, hitPoints = 100, hitDice = "28d20 + 252")
    )
}

@Preview
@Composable
fun StatsBlockPreview() = Window {
    StatsBlock(stats = Stats(armorClass = 0, hitPoints = 0, hitDice = "teasdas"))
}
