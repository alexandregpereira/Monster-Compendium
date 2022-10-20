/*
 * Copyright (C) 2022 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.data.spell.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class SchoolOfMagicDto {
    @SerialName("ABJURATION")
    ABJURATION,
    @SerialName("CONJURATION")
    CONJURATION,
    @SerialName("DIVINATION")
    DIVINATION,
    @SerialName("ENCHANTMENT")
    ENCHANTMENT,
    @SerialName("EVOCATION")
    EVOCATION,
    @SerialName("ILLUSION")
    ILLUSION,
    @SerialName("NECROMANCY")
    NECROMANCY,
    @SerialName("TRANSMUTATION")
    TRANSMUTATION,
}
