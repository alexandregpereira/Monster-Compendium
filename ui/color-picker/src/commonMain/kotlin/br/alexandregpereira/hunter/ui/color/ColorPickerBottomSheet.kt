package br.alexandregpereira.hunter.ui.color

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.localization.AppLocalization
import br.alexandregpereira.hunter.ui.compose.AppButton
import br.alexandregpereira.hunter.ui.compose.BottomSheet
import br.alexandregpereira.hunter.ui.compose.PreviewWindow
import br.alexandregpereira.hunter.ui.compose.ScreenHeader
import br.alexandregpereira.hunter.ui.util.toColor
import br.alexandregpereira.hunter.ui.util.toHex
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorPickerController
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import org.koin.compose.koinInject

@Composable
fun ColorPickerBottomSheet() {
    val colorPickerDispatcher = rememberColorPickerDispatcher(init = true)
    var isOpen by remember { mutableStateOf(false) }
    var initialColorHex by remember { mutableStateOf("") }
    val controller = rememberColorPickerController()
    LaunchedEffect(colorPickerDispatcher) {
        colorPickerDispatcher.events.collect { colorHex ->
            initialColorHex = colorHex
            controller.selectByColor(colorHex.toColor(), fromUser = false)
            isOpen = true
        }
    }

    val strings = rememberStrings(isOpen)
    ColorPickerBottomSheet(
        isOpen = isOpen,
        title = strings.bottomSheetTitle,
        buttonText = strings.bottonSheetButonText,
        initialColorHex = initialColorHex,
        controller = controller,
        onClose = { isOpen = false },
        onColorPicked = { color ->
            colorPickerDispatcher.onColorPicked(color)
            isOpen = false
        }
    )
}

@Composable
private fun ColorPickerBottomSheet(
    isOpen: Boolean,
    title: String,
    buttonText: String,
    initialColorHex: String,
    controller: ColorPickerController,
    onClose: () -> Unit,
    onColorPicked: (color: String) -> Unit,
) {
    BottomSheet(
        opened = isOpen,
        topSpaceHeight = 220.dp,
        onClose = onClose,
    ) {
        val initialColor = remember(initialColorHex) { initialColorHex.toColor() }
        var colorHex by remember(initialColorHex) { mutableStateOf(initialColorHex) }
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            ScreenHeader(
                title = title,
            )
            HsvColorPicker(
                modifier = Modifier
                    .height(200.dp),
                initialColor = initialColor,
                controller = controller,
                onColorChanged = { envelope ->
                    if (envelope.fromUser) colorHex = envelope.color.toHex()
                }
            )
            BrightnessSlider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(32.dp),
                initialColor = initialColor,
                controller = controller,
            )
            ColorTextField(
                text = colorHex,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { newColorHex ->
                    colorHex = newColorHex
                    if (newColorHex.removePrefix("#").length == 6) {
                        controller.selectByColor(newColorHex.toColor(), fromUser = false)
                    }
                }
            )
            AppButton(
                text = buttonText,
                onClick = {
                    onColorPicked(colorHex)
                }
            )
        }
    }
}

@Composable
private fun rememberStrings(isOpen: Boolean): ColorPickerStrings {
    val appLocalization = koinInject<AppLocalization>()
    return remember(isOpen) {
        appLocalization.getLanguage().getStrings()
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun ColorPickerBottomSheetPreview() = PreviewWindow {
    ColorPickerBottomSheet(
        isOpen = true,
        title = "Color Picker",
        buttonText = "Pick",
        initialColorHex = "#FAF9F8",
        controller = rememberColorPickerController(),
        onClose = {},
        onColorPicked = {},
    )
}
