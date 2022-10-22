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

package br.alexandregpereira.hunter.data.spell.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SpellDto(
    @SerialName("index")
    val index: String,
    @SerialName("name")
    val name: String,
    @SerialName("level")
    val level: Int,
    @SerialName("casting_time")
    val castingTime: String,
    @SerialName("components")
    val components: String,
    @SerialName("duration")
    val duration: String,
    @SerialName("range")
    val range: String,
    @SerialName("ritual")
    val ritual: Boolean,
    @SerialName("concentration")
    val concentration: Boolean,
    @SerialName("saving_throw_type")
    val savingThrowType: SavingThrowTypeDto?,
    @SerialName("damage_type")
    val damageType: String?,
    @SerialName("school")
    val school: SchoolOfMagicDto,
    @SerialName("description")
    val description: String,
    @SerialName("higher_level")
    val higherLevel: String?
)
