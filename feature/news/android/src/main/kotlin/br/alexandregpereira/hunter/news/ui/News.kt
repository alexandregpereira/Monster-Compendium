package br.alexandregpereira.hunter.news.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.ui.compose.CoilImage

@Composable
internal fun News(
    newsItemState: NewsItemState,
) = Column {
    Text(text = newsItemState.title)

    Spacer(modifier = Modifier.height(8.dp))

    Text(text = newsItemState.description)
}

@Preview
@Composable
private fun NewsPreview() {
    News(
        newsItemState = NewsItemState(
            id = "1",
            title = "Title",
            description = "Description",
            url = "https://www.google.com.br",
        )
    )
}
