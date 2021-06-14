/*
 * Hunter - DnD 5th edition monster compendium application
 * Copyright (C) 2021 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.bestiary

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

@Serializable
data class SourceItem(
    val name: String,
    val acronym: String,
    val monster: List<Monster>,
//    val monsterFluff: List<MonsterFluff>,
//    val legendaryGroups: List<Any>
)

@Serializable
data class Monster(
    val name: String,
    val isNpc: Boolean = false,
    val srd: Boolean = false,
    @Serializable(with = CrSerializer::class)
    val cr: String? = null,
    val sourceName: String = "",
    val source: String,
    val page: Int? = null,
    val size: MonsterSize,
    @Serializable(with = TypeSerializer::class)
    val type: Type,
    @Serializable(with = AlignmentSerializer::class)
    val alignment: List<String> = emptyList(),
    @Serializable(with = AcSerializer::class)
    val ac: Int,
    val hp: Hp,
    @Serializable(with = SpeedSerializer::class)
    val speed: Speed,
    val str: Int,
    val dex: Int,
    val con: Int,
    val int: Int,
    val wis: Int,
    val cha: Int,
    val senses: List<String> = emptyList(),
    @Serializable(with = PassiveSerializer::class)
    val passive: String? = null,
    @Serializable(with = ResistSerializer::class)
    val resist: List<String> = emptyList(),
    @Serializable(with = ImmuneSerializer::class)
    val immune: List<String> = emptyList(),
    @Serializable(with = VulnerableSerializer::class)
    val vulnerable: List<String> = emptyList(),
    val conditionImmune: List<String> = emptyList(),
    val trait: List<Action> = emptyList(),
    val action: List<Action> = emptyList(),
    val hasToken: Boolean = false,
    val senseTags: List<String> = emptyList(),
    val damageTags: List<String> = emptyList(),
    val miscTags: List<String> = emptyList(),
    val save: Save? = null,
    @Serializable(with = SkillSerializer::class)
    val skill: Map<String, String> = emptyMap(),
)

@Serializable
data class Save(
    val str: String? = null,
    val dex: String? = null,
    val con: String? = null,
    val int: String? = null,
    val wis: String? = null,
    val cha: String? = null,
)

@Serializable
data class Type(
    val type: String,
    @Serializable(with = TypeTagsSerializer::class)
    val tags: List<String> = emptyList(),
)

@Serializable
enum class MonsterSize {
    @SerialName("T")
    TINY,

    @SerialName("S")
    SMALL,

    @SerialName("M")
    MEDIUM,

    @SerialName("L")
    LARGE,

    @SerialName("H")
    HUGE,

    @SerialName("G")
    GARGANTUAN
}

@Serializable
data class Alignment(
    val alignments: List<String> = emptyList(),
)

data class MonsterFluff(
    val name: String,
    val source: String,
    val entries: List<Any>,
    val images: List<Image>,
)

@Serializable
data class Ac(
    val ac: Int? = null,
    val from: List<String> = emptyList(),
)

@Serializable
data class Hp(
    val average: Int? = null,
    val formula: String? = null,
)

@Serializable
data class Speed(
    @Serializable(with = WalkSerializer::class)
    val walk: Int? = null,
    @Serializable(with = WalkSerializer::class)
    val climb: Int? = null,
    @Serializable(with = WalkSerializer::class)
    val fly: Int? = null,
    @Serializable(with = WalkSerializer::class)
    val burrow: Int? = null,
    @Serializable(with = WalkSerializer::class)
    val swim: Int? = null,
    val canHover: Boolean = false,
)

@Serializable
data class Action(
    val name: String,
//    @Serializable(with = ActionEntrySerializer::class)
//    val entries: List<ActionEntry>
)

@Serializable
sealed class ActionEntry {

    @Serializable
    @SerialName("value")
    data class ActionValue(val value: String) : ActionEntry()

    @Serializable
    @SerialName("list")
    data class ActionDetail(
        val style: String,
        val items: List<Item>,
    ) : ActionEntry()
}

@Serializable
data class Image(
    val type: String,
    val href: Href,
)

@Serializable
data class Href(
    val type: String,
    val path: String,
)

@Serializable
data class Item(
    val type: String,
    val name: String,
    val entry: String,
)

object TypeSerializer : JsonTransformingSerializer<Type>(Type.serializer()) {
    // If response is not an array, then it is a single object that should be wrapped into the array
    override fun transformDeserialize(element: JsonElement): JsonElement {
        return when (element) {
            is JsonPrimitive -> {
                buildJsonObject {
                    put("type", element)
                }
            }
            is JsonObject -> element
            else -> throw IllegalAccessException("WTF")
        }
    }
}

object AcSerializer : JsonTransformingSerializer<Int>(Int.serializer()) {

    override fun transformDeserialize(element: JsonElement): JsonElement {
        if (element !is JsonArray) return JsonArray(emptyList())

        return when (val firstElement = element.first()) {
            is JsonObject -> {
                firstElement["ac"] ?: JsonPrimitive(0)
            }
            is JsonPrimitive -> firstElement
            else -> throw IllegalAccessException("WTF")
        }
    }
}

object AlignmentSerializer : JsonTransformingSerializer<List<String>>(
    ListSerializer(String.serializer())
) {

    override fun transformDeserialize(element: JsonElement): JsonElement {
        if (element !is JsonArray) return JsonArray(emptyList())

        val firstElement = element.first()
        if (firstElement is JsonObject) {
            return firstElement["alignment"] as? JsonArray ?: JsonArray(emptyList())
        }

        return element
    }
}

object TypeTagsSerializer : JsonTransformingSerializer<List<String>>(
    ListSerializer(String.serializer())
) {

    override fun transformDeserialize(element: JsonElement): JsonElement {
        if (element !is JsonArray) return JsonArray(emptyList())

        return element.map {
            when (it) {
                is JsonObject -> it["tag"]!!
                is JsonPrimitive -> it
                else -> throw IllegalAccessException("WTF")
            }
        }.run {
            JsonArray(this)
        }
    }
}

object ActionEntrySerializer : JsonTransformingSerializer<List<ActionEntry>>(
    ListSerializer(ActionEntry.serializer())
) {

    override fun transformDeserialize(element: JsonElement): JsonElement {
        if (element !is JsonArray) return JsonArray(emptyList())

        return element.map { elementArrayItem ->
            when (elementArrayItem) {
                is JsonPrimitive -> buildJsonObject {
                    put("type", "value")
                    put("value", elementArrayItem)
                }
                is JsonObject -> elementArrayItem
                else -> throw IllegalAccessException("WTF")
            }
        }.run {
            JsonArray(this)
        }
    }
}

object ResistSerializer : ResistanceSerializer("resist")
object VulnerableSerializer : ResistanceSerializer("vulnerable")
object ImmuneSerializer : ResistanceSerializer("immune")

abstract class ResistanceSerializer(private val key: String) :
    JsonTransformingSerializer<List<String>>(
        ListSerializer(String.serializer())
    ) {

    override fun transformDeserialize(element: JsonElement): JsonElement {
        if (element !is JsonArray) return JsonArray(emptyList())

        val acc = mutableListOf<JsonElement>()
        element.forEach { elementArrayItem ->
            when (elementArrayItem) {
                is JsonObject -> {
                    if (elementArrayItem.contains(key)) {
                        acc.addAll(elementArrayItem[key] as JsonArray)
                        elementArrayItem["note"]?.let { acc.add(it) }
                    } else if (elementArrayItem.contains("special")) {
                        acc.add(elementArrayItem["special"] as JsonPrimitive)
                    }
                }
                is JsonPrimitive -> acc.add(elementArrayItem)
                else -> throw IllegalAccessException("WTF")
            }
        }

        return acc.run {
            JsonArray(this)
        }
    }
}

object WalkSerializer : JsonTransformingSerializer<Int>(Int.serializer()) {

    override fun transformDeserialize(element: JsonElement): JsonElement {
        return when (element) {
            is JsonObject -> {
                element["number"] ?: throw IllegalAccessException("WTF")
            }
            else -> element
        }
    }
}

object SpeedSerializer : JsonTransformingSerializer<Speed>(Speed.serializer()) {

    override fun transformDeserialize(element: JsonElement): JsonElement {
        return when (element) {
            is JsonPrimitive -> {
                buildJsonObject { put("walk", element) }
            }
            else -> element
        }
    }
}

object PassiveSerializer : JsonTransformingSerializer<String>(String.serializer()) {

    override fun transformDeserialize(element: JsonElement): JsonElement {
        return JsonPrimitive(element.run { this as JsonPrimitive }.content)
    }
}

object CrSerializer : JsonTransformingSerializer<String>(String.serializer()) {

    override fun transformDeserialize(element: JsonElement): JsonElement {
        return when (element) {
            is JsonObject -> {
                element["cr"]!!
            }
            else -> element
        }
    }
}

object SkillSerializer : JsonTransformingSerializer<Map<String, String>>(MapSerializer(
    String.serializer(), String.serializer()
)) {

    override fun transformDeserialize(element: JsonElement): JsonElement {
        if (element !is JsonObject) throw IllegalAccessException("WTF")

        return JsonObject(element.filter {
            it.key != "other"
        })
    }
}
