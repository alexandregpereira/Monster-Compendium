package br.alexandregpereira.hunter.shareContent.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.shareContent.state.ShareContentState
import br.alexandregpereira.hunter.ui.compose.AppButton
import br.alexandregpereira.hunter.ui.compose.AppTextField
import br.alexandregpereira.hunter.ui.compose.ScreenHeader

@Composable
internal fun ShareContentExportScreen(
    state: ShareContentState,
    onCopy: () -> Unit,
) {
    Spacer(modifier = Modifier.height(16.dp))

    ScreenHeader(title = state.strings.exportTitle)

    Spacer(modifier = Modifier.height(16.dp))

    AppTextField(
        text = state.contentToExportShort,
        label = state.strings.contentToImportLabel,
        enabled = false,
    )

    Spacer(modifier = Modifier.height(32.dp))

    AppButton(
        text = state.exportCopyButtonText,
        enabled = state.exportCopyButtonEnabled,
        onClick = onCopy,
    )
}
