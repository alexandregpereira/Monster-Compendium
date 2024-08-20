/*
 * Copyright (C) 2024 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.search.domain

internal enum class SearchKey(
    val key: String,
    val valueType: SearchValueType = SearchValueType.String,
    val hasOnPreview: Boolean = false,
) {
    Name("-name", hasOnPreview = true),
    Cr("-challenge", hasOnPreview = true, valueType = SearchValueType.Float),
    Legendary("-legendaryMonster", valueType = SearchValueType.Boolean),
    Spellcaster("-spellcaster", valueType = SearchValueType.Boolean),
    Spell("-spellName"),
    Lore("-lore"),
    Type("-type", hasOnPreview = true),
    Edited("-edited", valueType = SearchValueType.Boolean, hasOnPreview = true),
    Cloned("-cloned", valueType = SearchValueType.Boolean, hasOnPreview = true),
    Imported("-imported", valueType = SearchValueType.Boolean, hasOnPreview = true),
    Fly("-fly", valueType = SearchValueType.Boolean),
    Burrow("-burrow", valueType = SearchValueType.Boolean),
    Hover("-hover", valueType = SearchValueType.Boolean),
    Swim("-swim", valueType = SearchValueType.Boolean),
    Group("-group", hasOnPreview = true),
    Alignment("-alignment", hasOnPreview = true),
    Size("-size", hasOnPreview = true),
    Source("-source", hasOnPreview = true),
    DamageVulnerability("-damageVulnerability"),
    DamageResistance("-damageResistance"),
    DamageImmunity("-damageImmunity"),
    SpecialAbility("-specialAbility"),
    Action("-action"),
    LegendaryAction("-legendaryAction"),
    Senses("-senses", hasOnPreview = true),
    ImagePortrait("-imagePortrait", valueType = SearchValueType.Boolean, hasOnPreview = true),
    ImageLandscape("-imageHorizontal", valueType = SearchValueType.Boolean, hasOnPreview = true),
    ImageUrl("-imageUrl", hasOnPreview = true),
}

internal enum class SearchValueType {
    String,
    Boolean,
    Float
}
