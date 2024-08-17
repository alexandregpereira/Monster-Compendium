package br.alexandregpereira.hunter.ui.compose.form

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.ui.compose.Window
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun AddRemoveButtons(
    modifier: Modifier = Modifier,
    addText: String,
    removeText: String = "",
    onAdd: () -> Unit = {},
    onRemove: () -> Unit = {},
) = Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically,
) {
    AddButton(
        text = addText,
        modifier = Modifier.weight(1f),
        onClick = onAdd,
    )
    Spacer(modifier = Modifier.width(16.dp))
    Box(
        modifier = Modifier.weight(1f)
    ) {
        AnimatedRemoveButton(
            removeText = removeText,
            onRemove = onRemove,
        )
    }
}

@Composable
private fun AnimatedRemoveButton(
    removeText: String,
    onRemove: () -> Unit = {},
) = AnimatedVisibility(
    visible = removeText.isNotBlank(),
    enter = fadeIn(),
    exit = fadeOut(),
) {
    RemoveButton(
        text = removeText,
        onClick = onRemove,
    )
}

@Composable
internal fun AddButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit = {},
) = ItemButton(
    icon = Icons.Default.Add,
    modifier = modifier,
    text = text,
    onClick = onClick,
)

@Composable
internal fun RemoveButton(
    modifier: Modifier = Modifier,
    text: String = "",
    onClick: () -> Unit = {},
) = ItemButton(
    icon = Icons.Default.Delete,
    modifier = modifier,
    text = text,
    onClick = onClick,
)

@Composable
private fun ItemButton(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) = Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = modifier
        .fillMaxWidth()
        .clickable { onClick() },
) {
    Icon(
        imageVector = icon,
        contentDescription = text,
        modifier = Modifier
            .padding(end = 8.dp)
            .padding(vertical = 16.dp),
    )
    Text(
        text = text,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        modifier = Modifier.padding(vertical = 16.dp),
    )
}

@Preview
@Composable
private fun AddRemoveButtonsPreview() = Window {
    AddRemoveButtons(
        addText = "Add special ability",
        removeText = "Remove ability",
    )
}
