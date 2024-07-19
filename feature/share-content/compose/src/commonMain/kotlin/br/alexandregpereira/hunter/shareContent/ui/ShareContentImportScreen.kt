package br.alexandregpereira.hunter.shareContent.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.shareContent.state.ShareContentState
import br.alexandregpereira.hunter.ui.compose.AppButton
import br.alexandregpereira.hunter.ui.compose.AppTextField
import br.alexandregpereira.hunter.ui.compose.ScreenHeader

@Composable
internal fun ShareContentImportScreen(
    state: ShareContentState,
    onContentEncodedChanges: (String) -> Unit,
    onImport: () -> Unit,
) {
    Spacer(modifier = Modifier.height(16.dp))

    ScreenHeader(title = state.strings.importTitle)

    Spacer(modifier = Modifier.height(16.dp))

    AppTextField(
        text = state.contentToImport,
        onValueChange = onContentEncodedChanges,
        label = state.strings.contentToImportLabel,
    )

    state.importErrorMessage.takeIf { it.isNotBlank() }?.let { errorMessage ->
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = errorMessage,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
        )
    }

    Spacer(modifier = Modifier.height(32.dp))

    AppButton(
        text = state.strings.importButton,
        onClick = onImport,
    )
}
