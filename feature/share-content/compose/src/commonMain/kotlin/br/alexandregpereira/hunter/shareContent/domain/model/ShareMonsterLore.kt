package br.alexandregpereira.hunter.shareContent.domain.model

import kotlinx.serialization.Serializable

@Serializable
internal data class ShareMonsterLore(
    val index: String,
    val name: String,
    val entries: List<ShareMonsterLoreEntry>,
)

@Serializable
internal data class ShareMonsterLoreEntry(
    val title: String? = null,
    val description: String,
)
