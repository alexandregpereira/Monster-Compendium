package br.alexandregpereira.hunter.news.domain

import br.alexandregpereira.hunter.news.domain.model.News
import kotlinx.coroutines.flow.Flow

class GetNewsUseCase internal constructor(
    private val remoteRepository: NewsRemoteRepository
){

    fun invoke(): Flow<List<News>> {
        return remoteRepository.getNews()
    }
}
