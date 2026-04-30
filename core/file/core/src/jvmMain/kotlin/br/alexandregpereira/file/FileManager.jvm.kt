package br.alexandregpereira.file

import java.io.File

internal class JvmFileManager : FileManager {

    override suspend fun saveFileToAppStorage(
        bytes: ByteArray,
        fileName: String,
        fileType: FileType,
    ): String {
        return "file://" + imagesDirectory(fileType)
            .apply { mkdirs() }
            .let { File(it, fileName).also { f -> f.writeBytes(bytes) } }
            .absolutePath
    }
}

internal actual fun FileManager.getImageNamesFromAppStorage(fileType: FileType): List<String> {
    return imagesDirectory(fileType).listFiles()?.map { it.name } ?: emptyList()
}

internal actual fun FileManager.deleteImageFromAppStorage(fileName: String, fileType: FileType) {
    File(imagesDirectory(fileType), fileName).delete()
}

private fun imagesDirectory(fileType: FileType): File = File(
    System.getProperty("user.home"),
    ".monster-compendium/${getFileFolder(fileType)}"
)
