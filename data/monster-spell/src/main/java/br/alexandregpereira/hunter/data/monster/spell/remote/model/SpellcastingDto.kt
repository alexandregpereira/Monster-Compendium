/*
 * Copyright 2022 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.alexandregpereira.hunter.data.monster.spell.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SpellcastingDto(
    @SerialName("desc")
    val desc: String,
    @SerialName("type")
    val type: SpellcastingTypeDto,
    @SerialName("spells_by_group")
    val spellsByGroup: List<SpellUsageDto>
)

@Serializable
data class SpellUsageDto(
    @SerialName("group")
    val group: String,
    @SerialName("spells")
    val spells: List<SpellPreviewDto>
)

@Serializable
data class SpellPreviewDto(
    @SerialName("index")
    val index: String,
    @SerialName("name")
    val name: String,
    @SerialName("school")
    val school: SchoolOfMagicDto,
    @SerialName("level")
    val level: Int,
)

@Serializable
enum class SpellcastingTypeDto {
    @SerialName("SPELLCASTER")
    SPELLCASTER,
    @SerialName("INNATE")
    INNATE
}
