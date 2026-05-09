package br.alexandregpereira.file

import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.absolutePath
import io.github.vinceglb.filekit.delete
import io.github.vinceglb.filekit.name
import io.github.vinceglb.filekit.readBytes
import io.github.vinceglb.filekit.size

class FileEntry internal constructor(
    internal val platformFile: PlatformFile,
) {
    val name: String
        get() = platformFile.name

    val filePath: String
        get() = "file://" + platformFile.absolutePath()
            .removePrefix("file://")

    val size: Long
        get() = platformFile.size()

    override fun toString(): String {
        return platformFile.absolutePath()
    }
}

suspend fun FileEntry.delete() {
    platformFile.delete(mustExist = false)
}

fun FileEntry(
    path: String,
): FileEntry = FileEntry(
    platformFile = PlatformFile(path.removePrefix("file://")),
)

suspend fun FileEntry.readBytes(): ByteArray {
    return platformFile.readBytes()
}

fun PlatformFile.toFileEntry(): FileEntry {
    return FileEntry(this)
}
