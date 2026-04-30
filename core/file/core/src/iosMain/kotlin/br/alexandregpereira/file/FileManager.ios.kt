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

internal class IosFileManager : FileManager {

    @OptIn(BetaInteropApi::class)
    @ExperimentalForeignApi
    override suspend fun saveFileToAppStorage(
        bytes: ByteArray,
        fileName: String,
        fileType: FileType,
    ): String {
        val imagesDir = imagesDirectory(fileType)
        NSFileManager.defaultManager.createDirectoryAtPath(
            imagesDir,
            withIntermediateDirectories = true,
            attributes = null,
            error = null,
        )
        val filePath = "$imagesDir/$fileName"
        @OptIn(ExperimentalForeignApi::class)
        bytes.usePinned { pinned ->
            NSData.create(bytes = pinned.addressOf(0), length = bytes.size.toULong())
        }.writeToFile(filePath, atomically = true)
        return "file://$filePath"
    }
}

@OptIn(ExperimentalForeignApi::class)
internal actual fun FileManager.getImageNamesFromAppStorage(fileType: FileType): List<String> {
    return NSFileManager.defaultManager
        .contentsOfDirectoryAtPath(imagesDirectory(fileType), error = null)
        ?.mapNotNull { it as? String }
        ?: emptyList()
}

@OptIn(ExperimentalForeignApi::class)
internal actual fun FileManager.deleteImageFromAppStorage(fileName: String, fileType: FileType) {
    val filePath = "${imagesDirectory(fileType)}/$fileName"
    NSFileManager.defaultManager.removeItemAtPath(filePath, error = null)
}

private fun imagesDirectory(fileType: FileType): String {
    val documents = NSSearchPathForDirectoriesInDomains(
        NSDocumentDirectory, NSUserDomainMask, true
    ).first() as String
    return "$documents/${getFileFolder(fileType)}"
}
