package br.alexandregpereira.hunter.domain.repository

import kotlinx.coroutines.flow.Flow

interface ImageBaseUrlRepository {

    fun getImageBaseUrl(): Flow<String>
}
