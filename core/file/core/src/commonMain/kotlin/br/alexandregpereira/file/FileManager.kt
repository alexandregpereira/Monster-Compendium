package br.alexandregpereira.file

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlin.time.Clock

interface FileManager {

    suspend fun saveFileToAppStorage(
        bytes: ByteArray,
        fileName: String,
        fileType: FileType
    ): String
}

enum class FileType(val extension: String) {
    PNG(extension = "png"),
}

suspend fun FileManager.saveImageToAppStorage(bytes: ByteArray, imageName: String): String {
    return saveFileToAppStorage(bytes, imageName, FileType.PNG)
}

internal fun getFileFolder(fileType: FileType): String {
    return when (fileType) {
        FileType.PNG -> "images"
    }
}

internal expect fun FileManager.getImageNamesFromAppStorage(fileType: FileType): List<String>
internal expect fun FileManager.deleteImageFromAppStorage(fileName: String, fileType: FileType)

internal class FileManagerImpl internal constructor(
    private val platformFileManager: FileManager,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : FileManager {

    override suspend fun saveFileToAppStorage(
        bytes: ByteArray,
        fileName: String,
        fileType: FileType,
    ): String = withContext(dispatcher) {
        val files = platformFileManager.getImageNamesFromAppStorage(fileType)
        files.firstOrNull { it.startsWith(fileName) }?.let {
            platformFileManager.deleteImageFromAppStorage(fileName = it, fileType)
        }
        val fileNameWithTimestamp =
            "$fileName-${Clock.System.now().toEpochMilliseconds()}.${fileType.extension}"
        platformFileManager.saveFileToAppStorage(bytes, fileNameWithTimestamp, fileType)
    }
}
