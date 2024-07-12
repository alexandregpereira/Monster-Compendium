package br.alexandregpereira.hunter.ui.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ConfirmationBottomSheet(
    show: Boolean,
    description: String,
    buttonText: String,
    contentPadding: PaddingValues = PaddingValues(),
    onConfirmed: () -> Unit = {},
    onClosed: () -> Unit = {}
) = BottomSheet(
    opened = show,
    contentPadding = PaddingValues(
        top = 16.dp + contentPadding.calculateTopPadding(),
        bottom = 16.dp + contentPadding.calculateBottomPadding(),
        start = 16.dp,
        end = 16.dp,
    ),
    onClose = onClosed,
) {
    Spacer(modifier = Modifier.height(16.dp))

    ScreenHeader(
        title = description,
    )

    Spacer(modifier = Modifier.height(32.dp))

    AppButton(
        text = buttonText,
        onClick = onConfirmed
    )
}
