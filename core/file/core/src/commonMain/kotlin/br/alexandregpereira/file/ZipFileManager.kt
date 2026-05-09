package br.alexandregpereira.file

interface ZipFileManager {

    suspend fun createZipFile(
        zipEntryFiles: List<FileEntry>,
        zipFileName: String,
    ): String

    suspend fun extractZipFile(bytes: ByteArray): List<FileEntry>
}
