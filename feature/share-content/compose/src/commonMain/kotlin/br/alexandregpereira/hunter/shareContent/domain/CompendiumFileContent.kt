package br.alexandregpereira.hunter.shareContent.domain

import br.alexandregpereira.file.FileEntry
import br.alexandregpereira.hunter.shareContent.domain.model.ShareContent

internal data class CompendiumFileContent(
    val name: String,
    val shareContent: ShareContent,
    val images: List<FileEntry>,
    val sizeFormatted: String,
) {
    val imagesQuantity: Int get() = images.size
    val monstersQuantity: Int get() = shareContent.monsters?.size ?: 0
    val monstersLoreQuantity: Int get() = shareContent.monstersLore?.size ?: 0
    val spellsQuantity: Int get() = shareContent.spells?.size ?: 0
}
