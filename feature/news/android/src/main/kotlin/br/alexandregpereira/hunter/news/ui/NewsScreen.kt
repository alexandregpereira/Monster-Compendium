package br.alexandregpereira.hunter.news.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.alexandregpereira.hunter.ui.compose.AppButton
import br.alexandregpereira.hunter.ui.compose.BottomSheet
import br.alexandregpereira.hunter.ui.compose.ScreenHeader
import br.alexandregpereira.hunter.ui.theme.HunterTheme

@Composable
internal fun NewsScreen(
    newsState: NewsState,
    contentPadding: PaddingValues = PaddingValues(),
) = HunterTheme {

    BottomSheet(
        opened = true,
        contentPadding = contentPadding,
    ) {
        ScreenHeader(
            title = "What's new?",
            modifier = Modifier
        )

        newsState.news.forEach { newsItemState ->
            News(newsItemState = newsItemState)
        }

        if (newsState.shouldSync) {
            AppButton(text = "Sync")
        }
    }
}

@Preview
@Composable
private fun NewsScreenPreview() {
    NewsScreen(
        newsState = NewsState(
            news = listOf(
                NewsItemState(
                    id = "1",
                    title = "Title",
                    description = "Description",
                    url = "https://www.google.com.br",
                )
            ),
            shouldSync = true,
        )
    )
}
