package br.alexandregpereira.file

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask
import platform.Foundation.create
import platform.Foundation.writeToFile

internal class IosImageFileManager : ImageFileManager {

    @OptIn(BetaInteropApi::class)
    @ExperimentalForeignApi
    override suspend fun saveImageToAppStorage(bytes: ByteArray, imageName: String): String {
        val imagesDir = imagesDirectory()
        NSFileManager.defaultManager.createDirectoryAtPath(
            imagesDir,
            withIntermediateDirectories = true,
            attributes = null,
            error = null,
        )
        val filePath = "$imagesDir/$imageName"
        @OptIn(ExperimentalForeignApi::class)
        bytes.usePinned { pinned ->
            NSData.create(bytes = pinned.addressOf(0), length = bytes.size.toULong())
        }.writeToFile(filePath, atomically = true)
        return "file://$filePath"
    }
}

@OptIn(ExperimentalForeignApi::class)
internal actual fun ImageFileManager.getImageNamesFromAppStorage(): List<String> {
    return NSFileManager.defaultManager
        .contentsOfDirectoryAtPath(imagesDirectory(), error = null)
        ?.mapNotNull { it as? String }
        ?: emptyList()
}

@OptIn(ExperimentalForeignApi::class)
internal actual fun ImageFileManager.deleteImageFromAppStorage(fileName: String) {
    val filePath = "${imagesDirectory()}/$fileName"
    NSFileManager.defaultManager.removeItemAtPath(filePath, error = null)
}

private fun imagesDirectory(): String {
    val documents = NSSearchPathForDirectoriesInDomains(
        NSDocumentDirectory, NSUserDomainMask, true
    ).first() as String
    return "$documents/$IMAGES_FOLDER_NAME"
}
