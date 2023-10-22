package br.alexandregpereira.hunter.news.ui

data class NewsState(
    val news: List<NewsItemState> = emptyList(),
    val shouldSync: Boolean = false,
)

data class NewsItemState(
    val id: String,
    val title: String,
    val description: String,
    val url: String,
)
