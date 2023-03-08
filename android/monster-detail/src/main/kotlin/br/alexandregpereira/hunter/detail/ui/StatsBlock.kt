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
import br.alexandregpereira.hunter.ui.compose.Window

@Composable
fun StatsBlock(
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
