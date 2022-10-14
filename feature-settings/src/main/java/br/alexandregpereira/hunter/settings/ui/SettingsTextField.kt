package br.alexandregpereira.hunter.settings.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.ui.compose.AppTextField
import br.alexandregpereira.hunter.ui.compose.Window

@Composable
fun SettingsTextField(
    text: String,
    modifier: Modifier = Modifier,
    description: String = "",
    onValueChange: (String) -> Unit = {}
) = Column(modifier) {
    AppTextField(
        text = text,
        onValueChange = onValueChange
    )

    description.takeIf { it.isNotBlank() }?.let {
        Text(
            text = it,
            fontWeight = FontWeight.Light,
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}


@Preview
@Composable
private fun SettingsTextFieldPreview() = Window {
    Column {
        SettingsTextField(
            text = "",
        )
        SettingsTextField(
            text = "",
            description = "Something describing something",
            modifier = Modifier.padding(top = 16.dp)
        )
        SettingsTextField(
            text = "asda asdas adasasd as",
            description = "Something describing something",
            modifier = Modifier.padding(top = 16.dp)
        )
        SettingsTextField(
            text = "asda asdas adasasd as",
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}
