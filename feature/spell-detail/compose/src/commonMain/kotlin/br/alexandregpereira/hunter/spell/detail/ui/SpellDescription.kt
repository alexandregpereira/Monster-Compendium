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

package br.alexandregpereira.hunter.spell.detail.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.ui.compose.AppMarkdown
import br.alexandregpereira.hunter.ui.compose.Window

@Composable
internal fun SpellDescription(
    description: String,
    modifier: Modifier = Modifier,
    higherLevel: String? = null
) = SpellDescriptionMarkdown(
    description = description,
    modifier = modifier,
    higherLevel = higherLevel,
)

@Composable
private fun SpellDescriptionMarkdown(
    description: String,
    modifier: Modifier = Modifier,
    higherLevel: String? = null
) = Column(modifier) {
    AppMarkdown(
        content = description,
    )
    higherLevel?.let {
        AppMarkdown(
            content = "**_${strings.atHigherLevels}_** $it",
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Preview
@Composable
private fun SpellDescriptionPreview() = Window {
    SpellDescription(
        description = "asdadsas ads asda sd asd asdasdasd asd asd asd asd asd asd as dassd asdasdasa" +
                " dasdasdasdas ads asdasdasdas dasasdasdasdasdasdasdas dasdasd asd asdasdasdasd" +
                " dasdasdasdas ads asdasdasdas dasasdasdasdasdasdasdas dasdasd asd asdasdasdasd" +
                " dasdasdasdas ads asdasdasdas dasasdasdasdasdasdasdas dasdasd asd asdasdasdasd" +
                " dasdasdasdas ads asdasdasdas dasasdasdasdasdasdasdas dasdasd asd asdasdasdasd" +
                " dasdasdasdas ads asdasdasdas dasasdasdasdasdasdasdas dasdasd asd asdasdasdasd" +
                "asd asdasdasd asdasdasdasdasdasasdasdasdasdasdasdasdasdasd",
        higherLevel = "asdadsas ads asda sd asd asdasdasd asd asd asd asd asd asd as dassd asdasdasa" +
                " dasdasdasdas ads asdasdasdas dasasdasdasdasdasdasdas dasdasd asd asdasdasdasd" +
                " dasdasdasdas ads asdasdasdas dasasdasdasdasdasdasdas dasdasd asd asdasdasdasd"
    )
}
