package br.alexandregpereira.file

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlin.time.Clock

interface ImageFileManager {

    suspend fun saveImageToAppStorage(bytes: ByteArray, imageName: String): String
}

internal expect fun ImageFileManager.getImageNamesFromAppStorage(): List<String>
internal expect fun ImageFileManager.deleteImageFromAppStorage(fileName: String)

internal class ImageFileManagerImpl internal constructor(
    private val platformImageFileManager: ImageFileManager,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ImageFileManager {

    override suspend fun saveImageToAppStorage(
        bytes: ByteArray,
        imageName: String
    ): String = withContext(dispatcher) {
        val files = platformImageFileManager.getImageNamesFromAppStorage()
        files.firstOrNull { it.startsWith(imageName) }?.let {
            platformImageFileManager.deleteImageFromAppStorage(fileName = it)
        }
        val fileNameWithTimestamp = "$imageName-${Clock.System.now().toEpochMilliseconds()}.png"
        platformImageFileManager.saveImageToAppStorage(bytes, fileNameWithTimestamp)
    }
}

internal const val IMAGES_FOLDER_NAME = "images"
