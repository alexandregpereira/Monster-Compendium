package br.alexandregpereira.hunter.data.remote

import kotlinx.coroutines.flow.Flow

internal interface FileManager {

    fun readText(): Flow<String>

    fun writeText(text: String): Flow<Unit>
}