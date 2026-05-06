package br.alexandregpereira.hunter.shareContent.domain

import br.alexandregpereira.file.FileEntry
import br.alexandregpereira.file.FileManager
import br.alexandregpereira.file.FileType
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.shareContent.domain.mapper.ShareContentMapper
import br.alexandregpereira.hunter.shareContent.domain.model.ShareContent
import br.alexandregpereira.ktx.runCatching
import kotlinx.serialization.SerializationException

internal interface CompendiumFileManager {

    suspend fun getCompendiumFileContent(
        zipFile: FileEntry,
    ): CompendiumFileContent

    suspend fun getCompendiumFileContent(
        fileName: String,
        shareContent: ShareContent,
    ): CompendiumFileContent

    suspend fun createCompendiumFile(
        compendiumFileContent: CompendiumFileContent,
    ): String

    suspend fun deleteCompendiumFiles()
}

internal class CompendiumFileManagerImpl(
    private val fileManager: FileManager,
    private val shareContentMapper: ShareContentMapper,
) : CompendiumFileManager {

    override suspend fun getCompendiumFileContent(zipFile: FileEntry): CompendiumFileContent {
        val files = fileManager.extractZipFile(zipFile.content)
        val contentJsonByteArray = files.firstOrNull { it.name == "content.json" }
            ?.content
            ?: error("content.json not found in .compendium archive")
        val contentJson = contentJsonByteArray.decodeToString()

        val shareContent = runCatching { shareContentMapper.decodeFromJson(contentJson) }
            .getOrElse { cause ->
                when (cause) {
                    is SerializationException -> throw ImportContentException.InvalidContent(
                        content = contentJson,
                        cause = cause,
                    )

                    else -> throw cause
                }
            }

        val images = files.filter { file ->
            file.name.endsWith(".png") || file.name.endsWith(".webp")
        }.getMonsterImages(
            monsters = shareContent.monsters,
        )

        val contentSize = contentJsonByteArray.size + images.sumOf { it.file.content.size }
        return CompendiumFileContent(
            name = zipFile.name,
            shareContent = shareContent,
            monsterImages = images,
            sizeFormatted = contentSize.getSizeFormatted(),
        )
    }

    override suspend fun getCompendiumFileContent(
        fileName: String,
        shareContent: ShareContent,
    ): CompendiumFileContent {
        val contentJson = shareContent.getContentToExport()
        val monsterImagePaths = mutableListOf<String>()
        val monsterImages = mutableListOf<FileEntry>()
        shareContent.monsters?.getImagePaths()?.forEach { filePath ->
            val file = runCatching {
                fileManager.getFileFromAppStorage(filePath)
            }.getOrNull()

            file?.let {
                monsterImagePaths.add(filePath)
                monsterImages.add(file)
            }
        }
        val contentSize = contentJson.encodeToByteArray().size + monsterImages.sumOf {
            it.content.size
        }

        return CompendiumFileContent(
            name = fileName,
            shareContent = shareContent,
            monsterImages = monsterImages.getMonsterImages(shareContent.monsters),
            sizeFormatted = contentSize.getSizeFormatted(),
        )
    }

    override suspend fun createCompendiumFile(
        compendiumFileContent: CompendiumFileContent
    ): String {
        val jsonEntry = FileEntry(
            name = "content.json",
            content = compendiumFileContent.shareContent.getContentToExport().encodeToByteArray(),
        )
        return fileManager.createZipFile(
            zipEntryFiles = listOf(jsonEntry) + compendiumFileContent.monsterImageFiles,
            zipFileName = compendiumFileContent.name,
        )
    }

    override suspend fun deleteCompendiumFiles() {
        fileManager.deleteAllsFilesFromAppStorage(FileType.COMPENDIUM)
    }

    private fun List<Monster>.getImagePaths(): List<String> {
        return filter {
            it.imageData.url.startsWith("file://")
        }.map {
            it.imageData.url
        }
    }

    private fun List<FileEntry>.getMonsterImages(
        monsters: List<Monster>?,
    ): List<CompendiumFileContent.MonsterImage> {
        val monsterByImageName = monsters?.filter {
            it.imageData.url.startsWith("file://")
        }?.associateBy {
            it.imageData.url.substringAfterLast("/")
        }.orEmpty()
        val monsterImages = mutableListOf<CompendiumFileContent.MonsterImage>()
        this.forEach { monsterFileImage ->
            val monster = monsterByImageName[monsterFileImage.name]
            val monsterImage = CompendiumFileContent.MonsterImage(
                index = monster?.index,
                name = monster?.name,
                file = monsterFileImage,
            )
            monsterImages.add(monsterImage)
        }
        return monsterImages
    }

    private fun Int.getSizeFormatted(): String {
        return when {
            this >= 1024 * 1024 * 1024 -> (this / (1024 * 1024 * 1024)).toString() + " GB"
            this >= 1024 * 1024 -> (this / (1024 * 1024)).toString() + " MB"
            this >= 1024 -> (this / 1024).toString() + " KB"
            else -> "$this Bytes"
        }
    }

    private suspend fun ShareContent.getContentToExport(): String {
        val shareContent = this
        return shareContentMapper.encodeToJson(shareContent)
    }
}
