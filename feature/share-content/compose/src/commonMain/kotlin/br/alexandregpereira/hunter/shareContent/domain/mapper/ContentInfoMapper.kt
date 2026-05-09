package br.alexandregpereira.hunter.shareContent.domain.mapper

import br.alexandregpereira.hunter.shareContent.domain.CompendiumFileContentInfo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put


internal interface ContentInfoMapper {

    suspend fun decodeFromJson(contentJson: String): CompendiumFileContentInfo

    suspend fun encodeToJson(value: CompendiumFileContentInfo): String
}

internal class ContentInfoMapperImpl(
    private val json: Json,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) : ContentInfoMapper {

    override suspend fun decodeFromJson(contentJson: String): CompendiumFileContentInfo =
        withContext(dispatcher) {
            val jsonObject = json.decodeFromString<JsonObject>(contentJson)
            val contentTitle = jsonObject["contentTitle"]?.jsonPrimitive?.contentOrNull
            val contentDescription = jsonObject["contentDescription"]?.jsonPrimitive?.contentOrNull
            val fileSizeFormatted = jsonObject["fileSizeFormatted"]?.jsonPrimitive?.contentOrNull
                ?: error("fileSizeFormatted is required")
            val fileExtension = jsonObject["fileExtension"]?.jsonPrimitive?.contentOrNull
                ?: error("fileExtension is required")

            CompendiumFileContentInfo(
                contentTitle = contentTitle,
                contentDescription = contentDescription,
                fileSizeFormatted = fileSizeFormatted,
                fileExtension = fileExtension,
            )
        }

    override suspend fun encodeToJson(value: CompendiumFileContentInfo): String =
        withContext(dispatcher) {
            buildJsonObject {
                value.contentTitle?.takeUnless { it.isBlank() }?.let {
                    put("contentTitle", it)
                }
                value.contentDescription?.takeUnless { it.isBlank() }?.let {
                    put("contentDescription", it)
                }
                put("fileSizeFormatted", value.fileSizeFormatted)
                put("fileExtension", value.fileExtension)
            }.toString()
        }
}
