package br.alexandregpereira.hunter.shareContent.state

import br.alexandregpereira.hunter.shareContent.domain.CompendiumFileContent
import br.alexandregpereira.hunter.shareContent.domain.model.ShareContent
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

internal data class ShareContentExtractedState(
    val fileName: String = "",
    val isFileNameEditable: Boolean = false,
    val contentSize: String = "",
    val contentEntries: ImmutableList<ShareContentExtractedEntryState> = persistentListOf(),
)

internal data class ShareContentExtractedEntryState(
    val id: String = "",
    val quantity: String = "",
    val content: String = "",
    val enabled: Boolean = true,
    val contentWarning: String? = null,
)

internal fun CompendiumFileContent.toShareContentExtractedState(
    strings: ShareContentExtractedStrings
): ShareContentExtractedState {
    val contentEntries = buildContentEntries(
        shareContent = this.shareContent,
        monsterImages = this.monsterImages,
        strings = strings,
    )

    return ShareContentExtractedState(
        fileName = this.name,
        contentSize = this.sizeFormatted,
        contentEntries = contentEntries,
    )
}

internal fun buildContentEntries(
    shareContent: ShareContent,
    monsterImages: List<CompendiumFileContent.MonsterImage>,
    strings: ShareContentExtractedStrings,
): ImmutableList<ShareContentExtractedEntryState> {
    return buildList {
        addIfIsNotEmpty(
            id = "monsters",
            quantity = shareContent.monsters?.size ?: 0,
            singularLabel = strings.monster,
            pluralLabel = strings.monsters,
            content = shareContent.monsters
                ?.joinToString(", ") { it.name }
                .orEmpty(),
        )
        addIfIsNotEmpty(
            id = "images",
            quantity = monsterImages.size,
            singularLabel = strings.image,
            pluralLabel = strings.images,
            content = monsterImages
                .joinToString(", ") {
                    it.name ?: it.file.name
                },
        )
        addIfIsNotEmpty(
            id = "monstersLore",
            quantity = shareContent.monstersLore?.size ?: 0,
            singularLabel = strings.monsterLore,
            pluralLabel = strings.monstersLore,
            content = shareContent.monstersLore
                ?.joinToString(", ") { monsterLore ->
                    monsterLore.name.takeUnless { it.isBlank() } ?: monsterLore.index
                }
                .orEmpty(),
        )
        addIfIsNotEmpty(
            id = "spells",
            quantity = shareContent.spells?.size ?: 0,
            singularLabel = strings.spell,
            pluralLabel = strings.spells,
            content = shareContent.spells
                ?.joinToString(", ") { it.name }
                .orEmpty(),
        )
    }.toPersistentList()
}

private fun getContentQuantityFormatted(
    quantity: Int,
    singularLabel: String,
    pluralLabel: String
): String {
    if (quantity == 0) return ""
    return if (quantity == 1) {
        "$quantity $singularLabel"
    } else {
        "$quantity $pluralLabel"
    }
}

private fun MutableList<ShareContentExtractedEntryState>.addIfIsNotEmpty(
    id: String = "",
    quantity: Int,
    singularLabel: String,
    pluralLabel: String,
    content: String = "",
    enabled: Boolean = true,
    contentWarning: String? = null,
) {
    if (quantity > 0 || enabled.not()) {
        ShareContentExtractedEntryState(
            id = id,
            quantity = getContentQuantityFormatted(
                quantity = quantity,
                singularLabel = singularLabel,
                pluralLabel = pluralLabel,
            ),
            content = content,
            enabled = enabled,
            contentWarning = contentWarning,
        ).also {
            add(it)
        }
    }
}
