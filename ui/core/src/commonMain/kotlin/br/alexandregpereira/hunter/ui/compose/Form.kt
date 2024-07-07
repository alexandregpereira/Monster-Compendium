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
