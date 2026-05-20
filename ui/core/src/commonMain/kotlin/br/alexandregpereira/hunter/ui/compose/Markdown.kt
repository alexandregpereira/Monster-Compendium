package br.alexandregpereira.hunter.ui.compose

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.mikepenz.markdown.compose.components.markdownComponents
import com.mikepenz.markdown.compose.elements.MarkdownTable
import com.mikepenz.markdown.compose.elements.MarkdownTableHeader
import com.mikepenz.markdown.compose.elements.MarkdownTableRow
import com.mikepenz.markdown.m2.Markdown
import com.mikepenz.markdown.m2.elements.MarkdownCheckBox
import com.mikepenz.markdown.m2.markdownTypography

@Composable
fun AppMarkdown(
    content: String,
    modifier: Modifier = Modifier,
) {
    Markdown(
        content = content,
        typography = descriptionTypography,
        components = descriptionComponents,
        modifier = modifier,
    )
}

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