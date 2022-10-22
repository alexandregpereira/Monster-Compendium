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

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
enum class MonsterTypeDto {
    @SerialName("ABERRATION")
    ABERRATION,
    @SerialName("BEAST")
    BEAST,
    @SerialName("CELESTIAL")
    CELESTIAL,
    @SerialName("CONSTRUCT")
    CONSTRUCT,
    @SerialName("DRAGON")
    DRAGON,
    @SerialName("ELEMENTAL")
    ELEMENTAL,
    @SerialName("FEY")
    FEY,
    @SerialName("FIEND")
    FIEND,
    @SerialName("GIANT")
    GIANT,
    @SerialName("HUMANOID")
    HUMANOID,
    @SerialName("MONSTROSITY")
    MONSTROSITY,
    @SerialName("OOZE")
    OOZE,
    @SerialName("PLANT")
    PLANT,
    @SerialName("UNDEAD")
    UNDEAD
}