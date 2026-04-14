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
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.ui.compose.Window
import com.mikepenz.markdown.compose.components.markdownComponents
import com.mikepenz.markdown.compose.elements.MarkdownTable
import com.mikepenz.markdown.compose.elements.MarkdownTableHeader
import com.mikepenz.markdown.compose.elements.MarkdownTableRow
import com.mikepenz.markdown.m2.Markdown
import com.mikepenz.markdown.m2.elements.MarkdownCheckBox
import com.mikepenz.markdown.m2.markdownTypography

private val descriptionComponents
    @Composable get() = markdownComponents(
        checkbox = { MarkdownCheckBox(it.content, it.node, it.typography.text) },
        table = {
            MarkdownTable(
                content = it.content,
                node = it.node,
                style = it.typography.table,
                headerBlock = { content, node, tableWidth, style ->
                    MarkdownTableHeader(
                        content = content,
                        header = node,
                        tableWidth = tableWidth,
                        style = style,
                        maxLines = Int.MAX_VALUE,
                        overflow = TextOverflow.Clip,
                    )
                },
                rowBlock = { content, node, tableWidth, style ->
                    MarkdownTableRow(
                        content = content,
                        header = node,
                        tableWidth = tableWidth,
                        style = style,
                        maxLines = Int.MAX_VALUE,
                        overflow = TextOverflow.Clip,
                    )
                },
            )
        },
    )

private val descriptionTypography
    @Composable get() = markdownTypography(
        text = MaterialTheme.typography.body1.copy(
            fontWeight = FontWeight.Light,
            fontSize = 14.sp,
        ),
        paragraph = MaterialTheme.typography.body1.copy(
            fontWeight = FontWeight.Light,
            fontSize = 14.sp,
        ),
        h1 = MaterialTheme.typography.h1.copy(
            fontSize = 16.sp,
        ),
        h2 = MaterialTheme.typography.h2.copy(
            fontSize = 16.sp,
        ),
        h3 = MaterialTheme.typography.h3.copy(
            fontSize = 16.sp,
        ),
        h4 = MaterialTheme.typography.h4.copy(
            fontSize = 16.sp,
        ),
        h5 = MaterialTheme.typography.h5.copy(
            fontSize = 16.sp,
        ),
        h6 = MaterialTheme.typography.h6.copy(
            fontSize = 16.sp,
        ),
    )

@Composable
internal fun SpellDescription(
    description: String,
    modifier: Modifier = Modifier,
    higherLevel: String? = null
) = Column(modifier) {
    Markdown(
        content = description,
        typography = descriptionTypography,
        components = descriptionComponents,
    )
    higherLevel?.let {
        Markdown(
            content = "**_${strings.atHigherLevels}_** $it",
            typography = descriptionTypography,
            components = descriptionComponents,
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
                " dasdasdasdas ads asdasdasdas dasasdasdasdasdasdasdas dasdasd asd asdasdasdasd" +
                "asd asdasdasd asdasdasdasdasdasasdasdasdasdasdasdasdasdasd",
        higherLevel = "asdadsas ads asda sd asd asdasdasd asd asd asd asd asd asd as dassd asdasdasa" +
                " dasdasdasdas ads asdasdasdas dasasdasdasdasdasdasdas dasdasd asd asdasdasdasd" +
                " dasdasdasdas ads asdasdasdas dasasdasdasdasdasdasdas dasdasd asd asdasdasdasd"
    )
}
