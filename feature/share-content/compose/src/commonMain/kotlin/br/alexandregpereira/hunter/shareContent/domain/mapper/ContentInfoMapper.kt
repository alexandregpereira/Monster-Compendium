package br.alexandregpereira.hunter.shareContent.domain.mapper

import br.alexandregpereira.hunter.shareContent.domain.CompendiumFileContentInfo
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
) : ContentInfoMapper {

    override suspend fun decodeFromJson(contentJson: String): CompendiumFileContentInfo {
        val jsonObject = json.decodeFromString<JsonObject>(contentJson)
        val contentTitle = jsonObject["contentTitle"]?.jsonPrimitive?.contentOrNull
        val contentDescription = jsonObject["contentDescription"]?.jsonPrimitive?.contentOrNull
        val fileSizeFormatted = jsonObject["fileSizeFormatted"]?.jsonPrimitive?.contentOrNull
            ?: error("fileSizeFormatted is required")
        val fileExtension = jsonObject["fileExtension"]?.jsonPrimitive?.contentOrNull
            ?: error("fileExtension is required")

        return CompendiumFileContentInfo(
            contentTitle = contentTitle,
            contentDescription = contentDescription,
            fileSizeFormatted = fileSizeFormatted,
            fileExtension = fileExtension,
        )
    }

    override suspend fun encodeToJson(value: CompendiumFileContentInfo): String {
        return buildJsonObject {
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
