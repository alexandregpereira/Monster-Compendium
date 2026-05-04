package br.alexandregpereira.hunter.shareContent.domain

import br.alexandregpereira.file.FileEntry
import br.alexandregpereira.file.FileManager
import kotlinx.serialization.SerializationException

internal fun interface GetCompendiumFileContent {
    suspend operator fun invoke(
        zipBytes: ByteArray,
    ): CompendiumFileContent
}

internal class GetCompendiumFileContentUseCase(
    private val fileManager: FileManager,
) : GetCompendiumFileContent {

    override suspend fun invoke(zipBytes: ByteArray): CompendiumFileContent {
        val files = fileManager.extractZipFile(zipBytes)
        val contentJsonByteArray = files.firstOrNull { it.name == "content.json" }
            ?.content
            ?: error("content.json not found in .compendium archive")
        val contentJson = contentJsonByteArray.decodeToString()

        val images = files.filter { file ->
            file.name.endsWith(".png") || file.name.endsWith(".webp")
        }

        val shareContent = runCatching { json.decodeFromString<ShareContent>(contentJson) }
            .getOrElse { cause ->
                when (cause) {
                    is SerializationException -> throw ImportContentException.InvalidContent(
                        content = contentJson,
                        cause = cause,
                    )
                    else -> throw cause
                }
            }

        val contentSize = contentJsonByteArray.size + images.sumOf { it.content.size }
        return CompendiumFileContent(
            shareContent = shareContent,
            images = images,
            sizeFormatted = contentSize.getSizeFormatted(),
        )
    }

    private fun Int.getSizeFormatted(): String {
        return when {
            this >= 1024 * 1024 * 1024 -> (this / (1024 * 1024 * 1024)).toString() + " GB"
            this >= 1024 * 1024 -> (this / (1024 * 1024)).toString() + " MB"
            this >= 1024 -> (this / 1024).toString() + " KB"
            else -> "$this Bytes"
        }
    }
}

internal data class CompendiumFileContent(
    val shareContent: ShareContent,
    val images: List<FileEntry>,
    val sizeFormatted: String,
) {
    val imagesQuantity: Int get() = images.size
    val monstersQuantity: Int get() = shareContent.monsters?.size ?: 0
    val monstersLoreQuantity: Int get() = shareContent.monstersLore?.size ?: 0
    val spellsQuantity: Int get() = shareContent.spells?.size ?: 0
}
