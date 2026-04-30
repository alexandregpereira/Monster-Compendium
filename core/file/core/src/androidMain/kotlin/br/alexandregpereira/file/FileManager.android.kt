package br.alexandregpereira.file

import android.app.Application
import java.io.File

internal class AndroidFileManager(
    internal val app: Application,
) : FileManager {

    override suspend fun saveFileToAppStorage(
        bytes: ByteArray,
        fileName: String,
        fileType: FileType
    ): String {
        val dir = File(app.filesDir, getFileFolder(fileType)).apply { mkdirs() }
        return "file://" + File(dir, fileName).also {
            it.writeBytes(bytes)
        }.absolutePath
    }
}

internal actual fun FileManager.getImageNamesFromAppStorage(fileType: FileType): List<String> {
    val fileManager = this as AndroidFileManager
    val dir = File(fileManager.app.filesDir, getFileFolder(fileType))
    return dir.listFiles()?.map { it.name } ?: emptyList()
}

internal actual fun FileManager.deleteImageFromAppStorage(fileName: String, fileType: FileType) {
    val fileManager = this as AndroidFileManager
    val dir = File(fileManager.app.filesDir, getFileFolder(fileType))
    File(dir, fileName).delete()
}
