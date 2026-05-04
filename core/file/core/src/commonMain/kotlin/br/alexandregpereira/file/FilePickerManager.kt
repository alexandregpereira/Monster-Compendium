package br.alexandregpereira.file

import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.openFilePicker
import io.github.vinceglb.filekit.name
import io.github.vinceglb.filekit.readBytes

interface FilePickerManager {

    suspend fun pickFile(
        type: FileType,
    ): FileEntry?
}

internal class FileKitFilePickerManager : FilePickerManager {

    override suspend fun pickFile(
        type: FileType,
    ): FileEntry? {
        val file = FileKit.openFilePicker()
        return file?.let {
            FileEntry(
                name = it.name,
                content = it.readBytes(),
            )
        }
    }
}
