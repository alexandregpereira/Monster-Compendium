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

package br.alexandregpereira.hunter.domain.model

import kotlin.native.ObjCName

@ObjCName(name = "Speed", exact = true)
data class Speed(
    val hover: Boolean,
    val values: List<SpeedValue>,
)

@ObjCName(name = "SpeedValue", exact = true)
data class SpeedValue(
    val type: SpeedType,
    val valueFormatted: String
) {

    companion object {

        fun create(
            type: SpeedType = SpeedType.WALK,
            valueFormatted: String = "",
        ): SpeedValue {
            return SpeedValue(
                type = type,
                valueFormatted = valueFormatted
            )
        }
    }
}

@ObjCName(name = "SpeedType", exact = true)
enum class SpeedType {
    BURROW,
    CLIMB,
    FLY,
    WALK,
    SWIM,
}
