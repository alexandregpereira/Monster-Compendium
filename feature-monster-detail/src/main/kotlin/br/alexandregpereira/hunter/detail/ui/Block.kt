package br.alexandregpereira.hunter.detail.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.ui.theme.HunterTheme

@Composable
fun Block(
    modifier: Modifier = Modifier,
    title: String? = null,
    contentPaddingBottom: Dp = 0.dp,
    content: @Composable ColumnScope.() -> Unit
) = Column(
    modifier.background(color = MaterialTheme.colors.surface)
        .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 16.dp + contentPaddingBottom)
) {

    title?.let {
        Text(
            text = title,
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )
    }

    Column(Modifier.fillMaxWidth(), content = content)
}

@Preview
@Composable
fun BlockPreview() = HunterTheme {
    Block(title = "Title") {

    }
}