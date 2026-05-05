package br.alexandregpereira.hunter.shareContent.domain

import br.alexandregpereira.hunter.shareContent.domain.model.ShareMonster
import br.alexandregpereira.hunter.shareContent.domain.model.ShareMonsterLore
import br.alexandregpereira.hunter.shareContent.domain.model.ShareSpell
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
internal data class ShareContent(
    val monsters: List<ShareMonster>? = null,
    val monstersLore: List<ShareMonsterLore>? = null,
    val spells: List<ShareSpell>? = null,
) {
    val version: Int = CURRENT_VERSION

    companion object {
        const val CURRENT_VERSION = 1
    }
}

internal val json = Json {
    ignoreUnknownKeys = true
    explicitNulls = false
}
