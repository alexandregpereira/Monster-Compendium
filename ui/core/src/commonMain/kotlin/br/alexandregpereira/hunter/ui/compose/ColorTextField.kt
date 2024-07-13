package br.alexandregpereira.hunter.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.ui.util.toColor

@Composable
fun ColorTextField(
    text: String,
    modifier: Modifier = Modifier,
    label: String = "",
    enabled: Boolean = true,
    onValueChange: (String) -> Unit = {},
)  = AppTextField(
    text = text,
    modifier = modifier,
    label = label,
    enabled = enabled,
    onValueChange = onValueChange,
    leadingIcon = {
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(text.toColor(), shape = CircleShape)
                .border(1.dp, MaterialTheme.colors.background, shape = CircleShape)
        )
    }
)
