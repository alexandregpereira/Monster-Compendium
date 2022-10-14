package br.alexandregpereira.hunter.ui.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AppButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit = {}
) {
    Button(
        enabled = enabled,
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = MaterialTheme.colors.surface,
        ),
        modifier = modifier
            .height(48.dp)
            .fillMaxWidth(),
        onClick = onClick
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
        )
    }
}

@Preview
@Composable
private fun AppButtonPreview() = Window {
    Column {
        AppButton(text = "Text")
        AppButton(
            text = "Text Disabled",
            enabled = false,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}
