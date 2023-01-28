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

package br.alexandregpereira.hunter.data.monster.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AbilityScoreDto(
    @SerialName("type")
    val type: AbilityScoreTypeDto,
    @SerialName("value")
    val value: Int,
    @SerialName("modifier")
    val modifier: Int
)

@Serializable
data class SavingThrowDto(
    @SerialName("index")
    val index: String,
    @SerialName("type")
    val type: AbilityScoreTypeDto,
    @SerialName("modifier")
    val modifier: Int
)

@Serializable
data class SkillDto(
    @SerialName("index")
    val index: String,
    @SerialName("modifier")
    val modifier: Int,
    @SerialName("name")
    val name: String
)

@Serializable
enum class AbilityScoreTypeDto {
    @SerialName("STRENGTH")
    STRENGTH,
    @SerialName("DEXTERITY")
    DEXTERITY,
    @SerialName("CONSTITUTION")
    CONSTITUTION,
    @SerialName("INTELLIGENCE")
    INTELLIGENCE,
    @SerialName("WISDOM")
    WISDOM,
    @SerialName("CHARISMA")
    CHARISMA
}
