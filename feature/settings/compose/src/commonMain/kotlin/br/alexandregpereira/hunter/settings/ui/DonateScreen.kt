package br.alexandregpereira.hunter.settings.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.settings.DonateState
import br.alexandregpereira.hunter.ui.compose.AppButton
import br.alexandregpereira.hunter.ui.compose.AppImageContentScale
import br.alexandregpereira.hunter.ui.compose.AppTextField
import br.alexandregpereira.hunter.ui.compose.BottomSheet
import br.alexandregpereira.hunter.ui.compose.CoilImage
import br.alexandregpereira.hunter.ui.compose.SectionTitle

@Composable
internal fun DonateScreen(
    isOpen: Boolean,
    state: DonateState,
    onClose: () -> Unit,
    onPixCodeCopy: () -> Unit
) = BottomSheet(
    opened = isOpen,
    topSpaceHeight = 0.dp,
    onClose = onClose,
) {
    val strings = state.strings
    Column {
        CoilImage(
            imageUrl = state.coverImageUrl,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .background(color = MaterialTheme.colors.background),
            contentDescription = "",
            contentScale = AppImageContentScale.Fit
        )

        Column(Modifier.padding(horizontal = 16.dp).padding(top = 8.dp, bottom = 16.dp)) {
            SectionTitle(strings.buyMeACoffee, isHeader = true)

            Text(
                text = strings.donateDescription,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 8.dp)
            )

            SectionTitle(
                title = strings.fromBrazil,
                isHeader = false,
                modifier = Modifier.padding(top = 24.dp)
            )

            AppTextField(
                text = state.pixCode,
                trailingIcon = null,
                label = strings.pixCopyAndPaste,
                enabled = false,
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            )

            val clipBoardManager = LocalClipboardManager.current
            AppButton(
                text = strings.copyPixCode,
                onClick = {
                    onPixCodeCopy()
                    clipBoardManager.setText(AnnotatedString(state.pixCode))
                },
                modifier = Modifier.padding(top = 16.dp)
            )

            SectionTitle(
                title = strings.fromOtherCountries,
                isHeader = false,
                modifier = Modifier.padding(top = 24.dp)
            )

            Text(
                text = strings.fromOtherCountriesDescription,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
