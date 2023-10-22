package br.alexandregpereira.hunter.news.domain

import br.alexandregpereira.hunter.news.domain.model.News
import kotlinx.coroutines.flow.Flow

interface NewsRemoteRepository {

    fun getNews(): Flow<List<News>>
}
