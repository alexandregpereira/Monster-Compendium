package br.alexandregpereira.hunter.shareContent.domain

import br.alexandregpereira.file.FileEntry
import br.alexandregpereira.hunter.shareContent.domain.model.ShareContent

internal typealias MonsterIndex = String

internal data class CompendiumFileContent(
    val name: String,
    val shareContent: ShareContent,
    val monsterImages: List<MonsterImage>,
    val contentInfo: CompendiumFileContentInfo,
) {
    val monsterImageFiles: List<FileEntry> = monsterImages.map {
        it.file
    }
    val imagesQuantity: Int get() = monsterImages.size
    val monstersQuantity: Int get() = shareContent.monsters?.size ?: 0
    val monstersLoreQuantity: Int get() = shareContent.monstersLore?.size ?: 0
    val spellsQuantity: Int get() = shareContent.spells?.size ?: 0

    val sizeFormatted: String get() = contentInfo.fileSizeFormatted

    data class MonsterImage(
        val index: MonsterIndex?,
        val name: String?,
        val file: FileEntry,
    )
}

internal data class CompendiumFileContentInfo(
    val contentTitle: String?,
    val contentDescription: String?,
    val fileSizeFormatted: String,
    val fileExtension: String = "compendium",
)
