package br.alexandregpereira.hunter.news.domain.model

data class News(
    val id: String,
    val title: String,
    val description: String,
    val shouldSync: Boolean,
)
