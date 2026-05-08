package br.alexandregpereira.file

import io.github.vinceglb.filekit.dialogs.FileKitType

enum class FileType(
    val folder: String
) {
    IMAGE(folder = "images"),
    COMPENDIUM(folder = "content"),
}

fun FileType.toFileKitType(): FileKitType {
    return when (this) {
        FileType.IMAGE -> FileKitType.Image
        FileType.COMPENDIUM -> FileKitType.File(
            extension = "compendium"
        )
    }
}
