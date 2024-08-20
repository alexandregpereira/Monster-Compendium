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

package br.alexandregpereira.hunter.ui.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.dp

@Composable
fun Form(
    modifier: Modifier = Modifier,
    title: String? = null,
    content: @Composable ColumnScope.() -> Unit,
) = Column(modifier) {
    if (!title.isNullOrBlank()) {
        ScreenHeader(
            title = title,
        )

        Spacer(modifier = Modifier.padding(top = 24.dp))
    }

    Layout(
        content = { content() }
    ) { measurables, constraints ->

        val placeables = measurables.map { it.measure(constraints) }
        val paddingTop = 16.dp.roundToPx()
        val height = placeables.sumOf {
            it.height + paddingTop
        } - paddingTop

        layout(constraints.maxWidth, height) {
            var yPosition = 0
            placeables.forEach {
                it.placeRelative(0, yPosition)
                yPosition += it.height + paddingTop
            }
        }
    }
}
