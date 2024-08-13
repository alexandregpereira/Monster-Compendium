package br.alexandregpereira.hunter.ui.compose

import androidx.compose.animation.EnterExitState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
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
    showCloseButton: Boolean = true,
    backgroundColor: Color = MaterialTheme.colors.surface,
    backHandlerEnabled: Boolean = isOpen,
    swipeVerticalState: SwipeVerticalState? = null,
    onAnimationStateChange: (EnterExitState) -> Unit = {},
    level: Int = 1,
    onClose: () -> Unit,
    content: @Composable () -> Unit
) {
    BackHandler(enabled = backHandlerEnabled, onBack = onClose)

    SwipeVerticalToDismiss(
        visible = isOpen,
        swipeVerticalState = swipeVerticalState,
        onAnimationStateChange = onAnimationStateChange,
        onClose = onClose
    ) {
        Window(backgroundColor = backgroundColor, level = level, modifier = modifier) {
            if (showCloseButton) {
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
    contentPaddingValues: PaddingValues = PaddingValues(),
    backgroundColor: Color = MaterialTheme.colors.surface,
    level: Int = 1,
    onClose: () -> Unit,
    content: @Composable () -> Unit
) = AppScreen(
    isOpen = isOpen,
    contentPaddingValues = contentPaddingValues,
    modifier = Modifier.fillMaxSize(),
    backgroundColor = backgroundColor,
    level = level,
    onClose = onClose,
    content = content
)

@Composable
fun BoxClosable(
    contentPaddingValues: PaddingValues = PaddingValues(),
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) = Box(modifier) {
    content()
    BoxCloseButton(
        showIcon = true,
        onClick = onClick,
        modifier = Modifier
            .padding(top = contentPaddingValues.calculateTopPadding())
    )
}

@Composable
fun BoxCloseButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    showIcon: Boolean = false,
) = Box(
    modifier = modifier.size(48.dp)
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = rememberRipple(bounded = false),
            onClick = onClick
        )
        .clip(CircleShape)
        .run {
            if (showIcon) {
                background(MaterialTheme.colors.surface.copy(alpha = .8f))
            } else this
        }
        .semantics { contentDescription = "Close" },
    contentAlignment = Alignment.Center
) {
    if (showIcon) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = null,
        )
    }
}
