package br.alexandregpereira.hunter.ui.color

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import br.alexandregpereira.hunter.ui.compose.animatePressed
import br.alexandregpereira.hunter.ui.theme.HunterTheme
import kotlinx.coroutines.launch

@Composable
fun ColorPicker(
    color: String,
    modifier: Modifier = Modifier,
    label: String = "",
    onColorPicked: (color: String) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val colorPickerDispatcher = rememberColorPickerDispatcher()

    Layout(
        modifier = modifier,
        content = {
            ColorTextField(
                text = color,
                enabled = true,
                showClearIcon = false,
                label = label,
                modifier = Modifier.layoutId("colorText")
            )

            Box(
                modifier.layoutId("colorButton").animatePressed(
                    onClick = {
                        coroutineScope.launch {
                            val color = colorPickerDispatcher.show(color)
                            onColorPicked(color)
                        }
                    }
                )
            )
        }
    ) { measurables, constraints ->
        val colorTextPlaceable = measurables.first { it.layoutId == "colorText" }
            .measure(constraints)

        val colorButtonPlaceable = measurables.first { it.layoutId == "colorButton" }
            .measure(Constraints.fixed(colorTextPlaceable.width, colorTextPlaceable.height))

        layout(constraints.maxWidth, colorTextPlaceable.height) {
            colorTextPlaceable.placeRelative(0, 0)
            colorButtonPlaceable.placeRelative(0, 0)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ColorPickerPreview() = HunterTheme {
    ColorPicker(
        color = "#010101",
        label = "Test",
        onColorPicked = {}
    )
}
