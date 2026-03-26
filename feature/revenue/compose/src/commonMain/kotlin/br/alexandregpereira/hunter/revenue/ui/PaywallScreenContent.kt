package br.alexandregpereira.hunter.revenue.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.ui.compose.AppButton

@Composable
internal fun PaywallScreenContent(
    subscribe: () -> Unit,
) {
    Column {
        Text(
            text = "Paywall",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier,
        )

        Text(
            text = "Description",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier,
        )

        AppButton(
            text = "Subscribe",
            modifier = Modifier,
            onClick = subscribe,
        )
    }
}

