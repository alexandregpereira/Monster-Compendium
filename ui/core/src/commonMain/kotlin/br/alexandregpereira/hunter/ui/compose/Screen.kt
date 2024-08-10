package br.alexandregpereira.hunter.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

@Composable
fun AppScreen(
    isOpen: Boolean,
    contentPaddingValues: PaddingValues = PaddingValues(),
    modifier: Modifier = Modifier,
    closeable: Boolean = true,
    backgroundColor: Color = MaterialTheme.colors.surface,
    onClose: () -> Unit,
    content: @Composable () -> Unit
) {
    BackHandler(enabled = isOpen, onBack = onClose)

    SwipeVerticalToDismiss(visible = isOpen, onClose = onClose, modifier = modifier) {
        Window(backgroundColor = backgroundColor) {
            if (closeable) {
                BoxClosable(
                    contentPaddingValues = contentPaddingValues,
                    onClick = onClose,
                    content = content,
                )
            } else {
                content()
            }
        }
    }
}

@Composable
fun AppFullScreen(
    isOpen: Boolean,
    contentPaddingValues: PaddingValues,
    onClose: () -> Unit,
    content: @Composable () -> Unit
) {
    AppScreen(isOpen = isOpen, contentPaddingValues = contentPaddingValues, onClose = onClose) {
        Window(Modifier.fillMaxSize()) {
            BoxClosable(
                contentPaddingValues = contentPaddingValues,
                onClick = onClose,
                content = content,
            )
        }
    }
}

@Composable
fun BoxClosable(
    contentPaddingValues: PaddingValues,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) = Box(modifier) {
    content()
    BoxCloseButton(
        onClick = onClick,
        modifier = Modifier
            .padding(top = contentPaddingValues.calculateTopPadding())
    )
}

@Composable
fun BoxCloseButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) = Box(
    modifier = modifier.size(48.dp)
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = rememberRipple(bounded = false),
            onClick = onClick
        )
        .clip(CircleShape)
        .semantics { contentDescription = "Close" }
)
