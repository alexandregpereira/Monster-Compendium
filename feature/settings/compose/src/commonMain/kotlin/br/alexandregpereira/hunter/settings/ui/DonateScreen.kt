package br.alexandregpereira.hunter.settings.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.settings.DonateState
import br.alexandregpereira.hunter.settings.DonateStrings
import br.alexandregpereira.hunter.settings.ui.resources.Res
import br.alexandregpereira.hunter.settings.ui.resources.ic_ko_fi_logo
import br.alexandregpereira.hunter.ui.compose.AppImageContentScale
import br.alexandregpereira.hunter.ui.compose.AppTextField
import br.alexandregpereira.hunter.ui.compose.BottomSheet
import br.alexandregpereira.hunter.ui.compose.CoilImage
import br.alexandregpereira.hunter.ui.compose.SectionTitle
import br.alexandregpereira.hunter.ui.compose.animatePressed
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun DonateScreen(
    isOpen: Boolean,
    state: DonateState,
    strings: DonateStrings,
    onClose: () -> Unit,
    onPixCodeCopy: () -> Unit,
    onPixKeyCopy: () -> Unit,
    onBuyMeCoffeeClick: () -> Unit,
) = BottomSheet(
    opened = isOpen,
    topSpaceHeight = 0.dp,
    onClose = onClose,
) {
    Column {
        CoilImage(
            imageUrl = state.coverImageUrl,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f),
            contentDescription = "",
            contentScale = AppImageContentScale.Fit
        )

        Column(Modifier.padding(horizontal = 12.dp).padding(top = 8.dp, bottom = 16.dp)) {
            SectionTitle(strings.buyMeACoffee, isHeader = false)

            Text(
                text = strings.donateDescription,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 8.dp)
            )

            BuyMeCoffeeButton(
                text = strings.donateAppName,
                label = strings.supportMe,
                onClick = onBuyMeCoffeeClick,
                modifier = Modifier.padding(top = 16.dp)
            )

            Text(
                text = strings.fromBrazil,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 24.dp)
            )

            Text(
                text = strings.fromBrazilDescription,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 8.dp)
            )

            val clipBoardManager = LocalClipboardManager.current
            CopyTextField(
                text = state.pixKey,
                label = strings.pixKey,
                buttonText = strings.copy,
                onClick = {
                    onPixKeyCopy()
                    clipBoardManager.setText(AnnotatedString(state.pixKey))
                },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            )

            CopyTextField(
                text = state.pixCode,
                label = strings.pixCopyAndPaste,
                buttonText = strings.copy,
                onClick = {
                    onPixCodeCopy()
                    clipBoardManager.setText(AnnotatedString(state.pixCode))
                },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            )
        }
    }
}

@Composable
private fun CopyTextField(
    label: String,
    text: String,
    buttonText: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) = Row(
    modifier = modifier,
    verticalAlignment = Alignment.Bottom
) {
    AppTextField(
        text = text,
        trailingIcon = null,
        label = label,
        enabled = false,
        modifier = Modifier.weight(1f)
    )
    Spacer(modifier = Modifier.width(8.dp))

    val buttonModifier = Modifier
        .height(56.dp)
        .animatePressed(
            onClick = onClick,
        )
        .clip(RoundedCornerShape(20))
        .background(MaterialTheme.colors.primary)
    Box(
        modifier = buttonModifier,
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = buttonText,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colors.onPrimary,
            fontSize = 16.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
private fun BuyMeCoffeeButton(
    text: String,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) = Row(
    modifier = modifier
        .height(54.dp)
        .animatePressed(
            onClick = onClick,
        )
        .clip(RoundedCornerShape(12.dp))
        .background(MaterialTheme.colors.primary),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.Center
) {
    val contentHorizontalPadding = 16.dp
    val contentVerticalPadding = 8.dp
    Spacer(modifier = Modifier.width(contentHorizontalPadding))
    Image(
        painter = painterResource(Res.drawable.ic_ko_fi_logo),
        contentDescription = null,
        modifier = Modifier.padding(vertical = contentVerticalPadding)
    )
    Spacer(modifier = Modifier.width(8.dp))
    Column(
        modifier = Modifier.padding(vertical = contentVerticalPadding)
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Light,
            fontSize = 12.sp,
            color = MaterialTheme.colors.onPrimary,
        )
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            color = MaterialTheme.colors.onPrimary,
        )
    }
    Spacer(modifier = Modifier.width(contentHorizontalPadding))
}
