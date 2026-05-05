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

package br.alexandregpereira.hunter.shareContent.state

interface ShareContentExtractedStrings {
    val images: String
    val image: String
    val spells: String
    val spell: String
    val monstersLore: String
    val monsterLore: String
    val monsters: String
    val monster: String
}

internal data class ShareContentExtractedEnStrings(
    override val images: String = "Images",
    override val image: String = "Image",
    override val spells: String = "Spells",
    override val spell: String = "Spell",
    override val monstersLore: String = "Lore entries",
    override val monsterLore: String = "Lore entry",
    override val monsters: String = "Creatures",
    override val monster: String = "Creature",
) : ShareContentExtractedStrings

internal data class ShareContentExtractedPtStrings(
    override val images: String = "Imagens",
    override val image: String = "Imagem",
    override val spells: String = "Magias",
    override val spell: String = "Magia",
    override val monstersLore: String = "Mitologias",
    override val monsterLore: String = "Mitologia",
    override val monsters: String = "Criaturas",
    override val monster: String = "Criatura",
) : ShareContentExtractedStrings


internal data class ShareContentExtractedEsStrings(
    override val images: String = "Imágenes",
    override val image: String = "Imagen",
    override val spells: String = "Magias",
    override val spell: String = "Magia",
    override val monstersLore: String = "Historias",
    override val monsterLore: String = "Historia",
    override val monsters: String = "Creatures",
    override val monster: String = "Creature",
) : ShareContentExtractedStrings
