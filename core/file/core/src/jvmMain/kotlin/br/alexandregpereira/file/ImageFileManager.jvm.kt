package br.alexandregpereira.file

import java.io.File

internal class JvmImageFileManager : ImageFileManager {

    override suspend fun saveImageToAppStorage(bytes: ByteArray, imageName: String): String {
        return "file://" + imagesDirectory()
            .apply { mkdirs() }
            .let { File(it, imageName).also { f -> f.writeBytes(bytes) } }
            .absolutePath
    }
}

internal actual fun ImageFileManager.getImageNamesFromAppStorage(): List<String> {
    return imagesDirectory().listFiles()?.map { it.name } ?: emptyList()
}

internal actual fun ImageFileManager.deleteImageFromAppStorage(fileName: String) {
    File(imagesDirectory(), fileName).delete()
}

private fun imagesDirectory(): File = File(
    System.getProperty("user.home"),
    ".monster-compendium/$IMAGES_FOLDER_NAME"
)
