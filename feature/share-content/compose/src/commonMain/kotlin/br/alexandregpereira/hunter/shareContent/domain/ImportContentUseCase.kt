package br.alexandregpereira.hunter.shareContent.domain

import br.alexandregpereira.hunter.domain.monster.lore.SaveMonstersLoreUseCase
import br.alexandregpereira.hunter.domain.spell.SaveSpells
import br.alexandregpereira.hunter.domain.usecase.SaveMonstersUseCase
import br.alexandregpereira.hunter.shareContent.domain.ShareContent.Companion.CURRENT_VERSION
import br.alexandregpereira.hunter.shareContent.domain.mapper.toMonster
import br.alexandregpereira.hunter.shareContent.domain.mapper.toMonsterLore
import br.alexandregpereira.hunter.shareContent.domain.mapper.toSpell
import br.alexandregpereira.hunter.shareContent.domain.model.ShareMonster
import br.alexandregpereira.hunter.shareContent.domain.model.ShareMonsterLore
import br.alexandregpereira.hunter.shareContent.domain.model.ShareSpell
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.single
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

internal val json = Json {
    ignoreUnknownKeys = true
    explicitNulls = false
}

internal fun interface ImportContent {
    operator fun invoke(contentJson: String): Flow<Unit>
}

internal fun ImportContent(
    saveMonsters: SaveMonstersUseCase,
    saveSpells: SaveSpells,
    saveMonstersLore: SaveMonstersLoreUseCase,
): ImportContent = ImportContent { contentJson ->
    flow {
        val content = runCatching { json.decodeFromString<ShareContent>(contentJson) }
            .getOrElse { cause ->
                when (cause) {
                    is SerializationException -> throw ImportContentException.InvalidContent(
                        content = contentJson,
                        cause = cause,
                    )
                    else -> throw cause
                }
            }
        content.monsters?.let { monsters ->
            saveMonsters(monsters.map { it.toMonster() }).single()
        }
        content.monstersLore?.let { monstersLore ->
            saveMonstersLore(monstersLore.map { it.toMonsterLore() }, isSync = false).single()
        }
        content.spells?.let { spells ->
            saveSpells(spells.map { it.toSpell() }).single()
        }
        emit(Unit)
    }
}

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

internal sealed class ImportContentException(message: String) : RuntimeException(message) {
    class InvalidContent(content: String, cause: Throwable) : ImportContentException(
        message = "SerializationException. " +
                "cause = ${cause.message}" +
                "current content version = $CURRENT_VERSION, " +
                "content imported version= ${content.replace("\n", "")}"
    )
}
