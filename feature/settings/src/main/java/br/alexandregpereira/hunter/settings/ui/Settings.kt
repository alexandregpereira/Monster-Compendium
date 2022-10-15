package br.alexandregpereira.hunter.settings.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.ui.compose.AppButton
import br.alexandregpereira.hunter.ui.compose.Window

@Composable
fun Settings(
    imageBaseUrl: String,
    alternativeSourceBaseUrl: String,
    modifier: Modifier = Modifier,
    saveButtonEnabled: Boolean = false,
    onImageBaseUrlChange: (String) -> Unit = {},
    onAlternativeSourceBaseUrlChange: (String) -> Unit = {},
    onSaveButtonClick: () -> Unit = {}
) = Column(modifier.padding(16.dp)) {

    Text(
        text = "Base URLs",
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        modifier = Modifier.padding(bottom = 16.dp)
    )

    Text(
        text = "Images Base URL",
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        modifier = Modifier
    )

    SettingsTextField(
        text = imageBaseUrl,
        onValueChange = onImageBaseUrlChange,
        modifier = Modifier.padding(top = 4.dp)
    )

    Text(
        text = "Alternative Source Base URL",
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        modifier = Modifier.padding(top = 16.dp)
    )

    SettingsTextField(
        text = alternativeSourceBaseUrl,
        onValueChange = onAlternativeSourceBaseUrlChange,
        modifier = Modifier.padding(top = 4.dp)
    )

    AppButton(
        text = "Save",
        enabled = saveButtonEnabled,
        onClick = onSaveButtonClick,
        modifier = Modifier.padding(top = 40.dp)
    )

    Text(
        text = "The app will be close to changes take effect",
        fontWeight = FontWeight.Light,
        fontStyle = FontStyle.Italic,
        fontSize = 12.sp,
        modifier = Modifier.padding(top = 8.dp).fillMaxWidth()
    )
}

@Preview
@Composable
private fun SettingsPreview() = Window {
    Settings(
        imageBaseUrl = "",
        alternativeSourceBaseUrl = ""
    )
}
