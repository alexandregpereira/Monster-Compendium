package br.alexandregpereira.hunter.shareContent.domain.mapper

import br.alexandregpereira.hunter.app.config.AppInfoProvider
import br.alexandregpereira.hunter.content.MonsterContentJsonMapper
import br.alexandregpereira.hunter.content.MonsterLoreContentJsonMapper
import br.alexandregpereira.hunter.content.SpellContentJsonMapper
import br.alexandregpereira.hunter.shareContent.domain.model.ShareContent
import br.alexandregpereira.ktx.runCatching
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

internal interface ShareContentMapper {

    suspend fun decodeFromJson(contentJson: String): ShareContent

    suspend fun encodeToJson(value: ShareContent): String

    sealed class ShareContentMapperException(
        message: String,
        cause: Throwable? = null
    ) : RuntimeException(message, cause)
}

internal class ShareContentMapperImpl(
    private val dispatcher: CoroutineDispatcher,
    private val json: Json,
    private val monsterContentJsonMapper: MonsterContentJsonMapper,
    private val monsterLoreContentJsonMapper: MonsterLoreContentJsonMapper,
    private val spellContentJsonMapper: SpellContentJsonMapper,
    private val appInfoProvider: AppInfoProvider,
) : ShareContentMapper {

    private val monstersKey = "monsters"
    private val monstersLoreKey = "monstersLore"
    private val spellsKey = "spells"
    private val minimumAppVersionCodeKey = "minimumAppVersionCode"

    override suspend fun decodeFromJson(
        contentJson: String
    ): ShareContent = withContext(dispatcher) {
        val contentJsonObject = decodeFromJsonOrThrow(contentJson) {
            json.decodeFromString<JsonObject>(contentJson)
        }
        val monsters = extractContentFromJson(
            key = monstersKey,
            parentJsonObject = contentJsonObject,
            parentJson = contentJson,
        ) {
            monsterContentJsonMapper.decodeFromJson(contentJson = it)
        }
        val monstersLore = extractContentFromJson(
            key = monstersLoreKey,
            parentJsonObject = contentJsonObject,
            parentJson = contentJson,
        ) {
            monsterLoreContentJsonMapper.decodeFromJson(contentJson = it)
        }
        val spells = extractContentFromJson(
            key = spellsKey,
            parentJsonObject = contentJsonObject,
            parentJson = contentJson,
        ) {
            spellContentJsonMapper.decodeFromJson(contentJson = it)
        }
        val minimumAppVersionCode = extractContentFromJson(
            key = minimumAppVersionCodeKey,
            parentJsonObject = contentJsonObject,
            parentJson = contentJson,
        ) {
            it.toInt()
        }

        if (minimumAppVersionCode > appInfoProvider.getVersionCode()) {
            throw UnsupportedVersion(
                version = minimumAppVersionCode,
                contentJson = contentJson,
            )
        }

        return@withContext ShareContent(
            monsters = monsters,
            monstersLore = monstersLore,
            spells = spells,
            minimumAppVersionCode = minimumAppVersionCode,
        )
    }

    override suspend fun encodeToJson(value: ShareContent): String = withContext(dispatcher) {
        val monstersContentJson = monsterContentJsonMapper
            .encodeToJson(value.monsters.orEmpty())
        val monstersLoreContentJson = monsterLoreContentJsonMapper
            .encodeToJson(value.monstersLore.orEmpty())
        val spellsContentJson = spellContentJsonMapper
            .encodeToJson(value.spells.orEmpty())
        return@withContext """
            {
                "$monstersKey":$monstersContentJson,
                "$monstersLoreKey":$monstersLoreContentJson,
                "$spellsKey":$spellsContentJson,
                "$minimumAppVersionCodeKey":${value.minimumAppVersionCode}
            }
        """.trimIndent()
    }

    private suspend fun <T> decodeFromJsonOrThrow(
        contentJson: String,
        key: String = "root",
        decode: suspend () -> T
    ): T {
        return runCatching {
            decode()
        }.getOrElse { cause ->
            throw FailToDecode(key, contentJson, cause)
        }
    }

    private suspend fun <T> extractContentFromJson(
        key: String,
        parentJsonObject: JsonObject,
        parentJson: String,
        decode: suspend (String) -> T
    ): T {
        return parentJsonObject[key]?.toString()?.let { contentJson ->
            decodeFromJsonOrThrow(contentJson, key = key, decode = { decode(contentJson) })
        } ?: throw MissingField(
            field = key,
            contentJson = parentJson,
        )
    }

    class FailToDecode(
        key: String,
        contentJson: String,
        cause: Throwable,
    ) : ShareContentMapper.ShareContentMapperException(
        message = "Failed to decode share content. key = $key. contentJson = $contentJson",
        cause = cause,
    )

    class MissingField(
        field: String,
        contentJson: String,
    ) : ShareContentMapper.ShareContentMapperException(
        message = "Missing field: $field on share content. contentJson = $contentJson",
    )

    class UnsupportedVersion(
        version: Int,
        contentJson: String,
    ) : ShareContentMapper.ShareContentMapperException(
        message = "Unsupported version: $version on share content. contentJson = $contentJson",
    )
}
