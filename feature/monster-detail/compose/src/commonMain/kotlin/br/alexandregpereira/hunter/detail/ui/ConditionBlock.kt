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

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.domain.model.ConditionType
import br.alexandregpereira.hunter.monster.detail.ConditionState
import br.alexandregpereira.hunter.ui.compose.animatePressed
import br.alexandregpereira.hunter.ui.theme.HunterTheme
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun ConditionBlock(
    conditions: List<ConditionState>,
    modifier: Modifier = Modifier,
    onConditionClicked: (String) -> Unit = {},
) = Block(
    title = strings.conditionImmunities,
    modifier = modifier,
) {

    ConditionGrid(conditions, onConditionClicked)
}

@Composable
private fun ConditionGrid(
    conditions: List<ConditionState>,
    onConditionClicked: (String) -> Unit = {},
) = Grid {

    conditions.forEach { condition ->
        Condition(
            condition = condition,
            modifier = Modifier.width(GridItemWidth).padding(horizontal = 8.dp),
            iconSize = 48.dp,
            onConditionClicked = onConditionClicked,
        )
    }
}

@Composable
internal fun Condition(
    condition: ConditionState,
    modifier: Modifier = Modifier,
    iconSize: Dp = 56.dp,
    onConditionClicked: (String) -> Unit = {},
) = IconInfo(
    title = condition.name,
    painter = painterResource(condition.type.toIcon()),
    iconSize = iconSize,
    modifier = modifier.animatePressed(
        pressedScale = .7f,
        onClick = {
            onConditionClicked(condition.index)
        }
    )
)

@Preview(showBackground = true)
@Composable
private fun ConditionBlockPreview() = HunterTheme {
    val conditions = listOf(
        ConditionState(
            index = "blinded",
            type = ConditionType.RESTRAINED,
            name = "Blinded"
        ),
        ConditionState(
            index = "blinded",
            type = ConditionType.FRIGHTENED,
            name = "Frightened"
        ),
        ConditionState(
            index = "blinded",
            type = ConditionType.FRIGHTENED,
            name = "Frightened"
        ),
        ConditionState(
            index = "blinded",
            type = ConditionType.FRIGHTENED,
            name = "Frightened"
        ),
        ConditionState(
            index = "blinded",
            type = ConditionType.FRIGHTENED,
            name = "Frightened"
        ),
        ConditionState(
            index = "blinded",
            type = ConditionType.FRIGHTENED,
            name = "Frightened"
        ),
        ConditionState(
            index = "blinded",
            type = ConditionType.FRIGHTENED,
            name = "Frightened"
        ),
    )
    ConditionBlock(conditions = conditions)
}
