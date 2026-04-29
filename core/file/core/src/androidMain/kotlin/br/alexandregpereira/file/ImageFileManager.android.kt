package br.alexandregpereira.file

import android.app.Application
import java.io.File

internal class AndroidImageFileManager(
    internal val app: Application,
) : ImageFileManager {

    override suspend fun saveImageToAppStorage(bytes: ByteArray, imageName: String): String {
        val dir = File(app.filesDir, IMAGES_FOLDER_NAME).apply { mkdirs() }
        return "file://" + File(dir, imageName).also {
            it.writeBytes(bytes)
        }.absolutePath
    }
}

internal actual fun ImageFileManager.getImageNamesFromAppStorage(): List<String> {
    val fileManager = this as AndroidImageFileManager
    val dir = File(fileManager.app.filesDir, IMAGES_FOLDER_NAME)
    return dir.listFiles()?.map { it.name } ?: emptyList()
}

internal actual fun ImageFileManager.deleteImageFromAppStorage(fileName: String) {
    val fileManager = this as AndroidImageFileManager
    val dir = File(fileManager.app.filesDir, IMAGES_FOLDER_NAME)
    File(dir, fileName).delete()
}
