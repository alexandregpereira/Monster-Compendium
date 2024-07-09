package br.alexandregpereira.hunter.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ClickableField(
    label: String,
    text: String,
    modifier: Modifier = Modifier,
    trailingIcon: @Composable (() -> Unit)? = {
        Icon(
            imageVector = Icons.Filled.KeyboardArrowDown,
            contentDescription = "",
            tint = MaterialTheme.colors.onSurface
        )
    },
    onClick: () -> Unit = {}
) {
    Box(modifier) {
        Column {
            AppTextField(text = text, label = label, trailingIcon = trailingIcon)
        }
        Spacer(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Transparent)
                .clickable(
                    onClick = onClick
                )
        )
    }
}

@Preview
@Composable
private fun ClickableFieldPreview() = Window {
    ClickableField(
        label = "Label",
        text = "",
        modifier = Modifier.padding(16.dp),
    )
}

@Preview
@Composable
private fun ClickableFieldWithValuePreview() = Window {
    ClickableField(
        label = "Label",
        text = "Value",
        modifier = Modifier.padding(16.dp),
    )
}
