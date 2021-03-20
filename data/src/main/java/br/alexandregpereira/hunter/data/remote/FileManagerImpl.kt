package br.alexandregpereira.hunter.data.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File

internal class FileManagerImpl(
    fileName: String
) : FileManager {

    private val file = File(fileName)

    override fun readText(): Flow<String> = flow {
        emit(file.readText())
    }

    override fun writeText(text: String): Flow<Unit> = flow {
        emit(file.writeText(text))
    }
}