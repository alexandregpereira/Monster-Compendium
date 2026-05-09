package br.alexandregpereira.hunter.shareContent.domain

import br.alexandregpereira.file.FileEntry
import br.alexandregpereira.file.FileManager
import br.alexandregpereira.file.FileType
import br.alexandregpereira.file.ZipFileManager
import br.alexandregpereira.file.delete
import br.alexandregpereira.file.readBytes
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.shareContent.domain.mapper.ContentInfoMapper
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
        fileName: String,
        compendiumFileContent: CompendiumFileContent,
    ): String

    suspend fun deleteCompendiumFiles()
}

internal class CompendiumFileManagerImpl(
    private val zipFileManager: ZipFileManager,
    private val fileManager: FileManager,
    private val shareContentMapper: ShareContentMapper,
    private val contentInfoMapper: ContentInfoMapper,
) : CompendiumFileManager {

    override suspend fun getCompendiumFileContent(zipFile: FileEntry): CompendiumFileContent {
        val files = zipFileManager.extractZipFile(zipFile.readBytes())
        val contentJsonByteArray = files.firstOrNull { it.name == "content.json" }
            ?.readBytes()
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

        val contentInfo = runCatching {
            val contentInfoJson = files.firstOrNull { it.name == "contentInfo.json" }
                ?.readBytes()?.decodeToString()
                ?: error("contentInfo.json not found in .compendium archive")
            contentInfoMapper.decodeFromJson(contentInfoJson)
        }.getOrNull() ?: CompendiumFileContentInfo(
            contentTitle = null,
            contentDescription = null,
            fileSizeFormatted = zipFile.size.getSizeFormatted(),
        )

        return CompendiumFileContent(
            name = zipFile.name,
            shareContent = shareContent,
            monsterImages = images,
            contentInfo = contentInfo,
        )
    }

    override suspend fun getCompendiumFileContent(
        fileName: String,
        shareContent: ShareContent,
    ): CompendiumFileContent {
        val contentJson = shareContentMapper.encodeToJson(shareContent)
        val monsterImages = mutableListOf<FileEntry>()
        shareContent.monsters?.getOriginalImagePaths()?.accumulateImages(
            monsterImages = monsterImages,
        )
        shareContent.monsters?.getCustomImagePaths()?.accumulateImages(
            monsterImages = monsterImages,
        )
        val contentSize = contentJson.encodeToByteArray().size + monsterImages.sumOf {
            it.size
        }

        val contentInfo = CompendiumFileContentInfo(
            contentTitle = "",
            contentDescription = "",
            fileSizeFormatted = contentSize.getSizeFormatted(),
        )

        return CompendiumFileContent(
            name = "$fileName.${contentInfo.fileExtension}",
            shareContent = shareContent,
            monsterImages = monsterImages.getMonsterImages(shareContent.monsters),
            contentInfo = contentInfo,
        )
    }

    override suspend fun createCompendiumFile(
        fileName: String,
        compendiumFileContent: CompendiumFileContent
    ): String {
        val contentEntry = fileManager.saveFileToAppStorage(
            bytes = shareContentMapper.encodeToJson(compendiumFileContent.shareContent)
                .encodeToByteArray(),
            fileName = "content.json",
            fileType = FileType.COMPENDIUM,
        ).let {
            FileEntry(it)
        }
        val contentInfoEntry = fileManager.saveFileToAppStorage(
            bytes = contentInfoMapper.encodeToJson(compendiumFileContent.contentInfo)
                .encodeToByteArray(),
            fileName = "contentInfo.json",
            fileType = FileType.COMPENDIUM,
        ).let {
            FileEntry(it)
        }

        return try {
            zipFileManager.createZipFile(
                zipEntryFiles = listOf(contentEntry, contentInfoEntry) +
                        compendiumFileContent.monsterImageFiles,
                zipFileName = fileName,
            )
        } catch (e: Exception) {
            contentEntry.delete()
            contentInfoEntry.delete()
            throw e
        }
    }

    override suspend fun deleteCompendiumFiles() {
        fileManager.deleteAllFilesFromAppStorage(FileType.COMPENDIUM)
    }

    private fun List<String>.accumulateImages(
        monsterImages: MutableList<FileEntry>,
    ) {
        forEach { filePath -> monsterImages.add(FileEntry(filePath)) }
    }

    private fun List<Monster>.getOriginalImagePaths(): List<String> {
        return filter {
            it.originalImageData.url.startsWith("file://")
        }.map {
            it.originalImageData.url
        }
    }

    private fun List<Monster>.getCustomImagePaths(): List<String> {
        return mapNotNull {
            it.customMonsterImage?.imageUrl?.takeIf { imageUrl ->
                imageUrl.startsWith("file://")
            }
        }
    }

    private fun List<FileEntry>.getMonsterImages(
        monsters: List<Monster>?,
    ): List<CompendiumFileContent.MonsterImage> {
        val monsterByImageName = monsters?.filter {
            it.originalImageData.url.startsWith("file://") ||
                    it.customMonsterImage?.imageUrl?.startsWith("file://") == true
        }?.associateBy {
            it.customMonsterImage?.imageUrl?.takeIf { imageUrl ->
                imageUrl.startsWith("file://")
            }?.substringAfterLast("/")
                ?: it.originalImageData.url.substringAfterLast("/")
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

    private fun Long.getSizeFormatted(): String {
        return when {
            this >= 1024 * 1024 * 1024 -> (this / (1024 * 1024 * 1024)).toString() + " GB"
            this >= 1024 * 1024 -> (this / (1024 * 1024)).toString() + " MB"
            this >= 1024 -> (this / 1024).toString() + " KB"
            else -> "$this Bytes"
        }
    }
}
