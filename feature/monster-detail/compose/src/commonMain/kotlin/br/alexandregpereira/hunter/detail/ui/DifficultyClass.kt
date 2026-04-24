package br.alexandregpereira.hunter.detail.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.ui.theme.HunterTheme

@Composable
internal fun DifficultyClass(
    value: Int,
    name: String,
    modifier: Modifier = Modifier,
    iconSize: Dp = 56.dp,
    alpha: Float = 0.7f,
) = Column(
    modifier
        .alpha(alpha),
    horizontalAlignment = Alignment.CenterHorizontally
) {

    Box(
        modifier = Modifier.size(iconSize),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = strings.dc,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
            )
            Text(
                text = value.toString(),
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
            )
        }
    }

    Text(
        text = name,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        modifier = Modifier.padding(top = 8.dp),
        textAlign = TextAlign.Center
    )
}

@Preview(showBackground = true)
@Composable
private fun DifficultyClassPreview() = HunterTheme {
    DifficultyClass(value = 10, name = "Dexterity")
}
