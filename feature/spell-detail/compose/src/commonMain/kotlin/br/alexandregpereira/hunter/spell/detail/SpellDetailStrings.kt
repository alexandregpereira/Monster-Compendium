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

package br.alexandregpereira.hunter.spell.detail

import br.alexandregpereira.hunter.localization.AppLocalization
import br.alexandregpereira.hunter.localization.Language
import br.alexandregpereira.hunter.strings.format

internal interface SpellDetailStrings {
    val subtitleCantrip: String
    val subtitleCantripWithRitual: String
    val subtitleLevel: String
    val subtitleLevelWithRitual: String
    val castingTime: String
    val range: String
    val components: String
    val duration: String
    val saveType: String
    val concentration: String
    val atHigherLevels: String
    val savingThrowStrength: String
    val savingThrowDexterity: String
    val savingThrowConstitution: String
    val savingThrowIntelligence: String
    val savingThrowWisdom: String
    val savingThrowCharisma: String
    val schoolAbjuration: String
    val schoolConjuration: String
    val schoolDivination: String
    val schoolEnchantment: String
    val schoolEvocation: String
    val schoolIllusion: String
    val schoolNecromancy: String
    val schoolTransmutation: String
    val cantrip: String
    val options: String
    val clone: String
    val edit: String
    val delete: String
    val resetToOriginal: String
    val cloneSpellName: String
    val save: String
    val deleteConfirmation: String
    val resetConfirmation: String
}

internal data class SpellDetailEnStrings(
    override val subtitleCantrip: String = "Cantrip, {0}",
    override val subtitleCantripWithRitual: String = "Cantrip (Ritual), {0}",
    override val subtitleLevel: String = "Level {0}, {1}",
    override val subtitleLevelWithRitual: String = "Level {0} (Ritual), {1}",
    override val castingTime: String = "Casting Time:",
    override val range: String = "Range:",
    override val components: String = "Components:",
    override val duration: String = "Duration:",
    override val saveType: String = "Saving Throw:",
    override val concentration: String = "Concentration",
    override val atHigherLevels: String = "At Higher Levels.",
    override val savingThrowStrength: String = "Strength",
    override val savingThrowDexterity: String = "Dexterity",
    override val savingThrowConstitution: String = "Constitution",
    override val savingThrowIntelligence: String = "Intelligence",
    override val savingThrowWisdom: String = "Wisdom",
    override val savingThrowCharisma: String = "Charisma",
    override val schoolAbjuration: String = "abjuration",
    override val schoolConjuration: String = "conjuration",
    override val schoolDivination: String = "divination",
    override val schoolEnchantment: String = "enchantment",
    override val schoolEvocation: String = "evocation",
    override val schoolIllusion: String = "illusion",
    override val schoolNecromancy: String = "necromancy",
    override val schoolTransmutation: String = "transmutation",
    override val cantrip: String = "Cantrip",
    override val options: String = "Options",
    override val clone: String = "Clone",
    override val edit: String = "Edit",
    override val delete: String = "Delete",
    override val resetToOriginal: String = "Reset to original",
    override val cloneSpellName: String = "Spell name",
    override val save: String = "Save",
    override val deleteConfirmation: String = "Are you sure you want to delete this spell?",
    override val resetConfirmation: String = "Are you sure you want to reset this spell to its original version?",
) : SpellDetailStrings

internal data class SpellDetailPtStrings(
    override val subtitleCantrip: String = "Truque, {0}",
    override val subtitleCantripWithRitual: String = "Truque (Ritual), {0}",
    override val subtitleLevel: String = "Nível {0}, {1}",
    override val subtitleLevelWithRitual: String = "Nível {0} (Ritual), {1}",
    override val castingTime: String = "Tempo de Conjuração:",
    override val range: String = "Alcance:",
    override val components: String = "Componentes:",
    override val duration: String = "Duração:",
    override val saveType: String = "Salvaguarda:",
    override val concentration: String = "Concentração",
    override val atHigherLevels: String = "Em Níveis Superiores.",
    override val savingThrowStrength: String = "Força",
    override val savingThrowDexterity: String = "Destreza",
    override val savingThrowConstitution: String = "Constituição",
    override val savingThrowIntelligence: String = "Inteligência",
    override val savingThrowWisdom: String = "Sabedoria",
    override val savingThrowCharisma: String = "Carisma",
    override val schoolAbjuration: String = "abjuração",
    override val schoolConjuration: String = "conjuração",
    override val schoolDivination: String = "adivinhação",
    override val schoolEnchantment: String = "encantamento",
    override val schoolEvocation: String = "evocação",
    override val schoolIllusion: String = "ilusão",
    override val schoolNecromancy: String = "necromancia",
    override val schoolTransmutation: String = "transmutação",
    override val cantrip: String = "Truque",
    override val options: String = "Opções",
    override val clone: String = "Clonar",
    override val edit: String = "Editar",
    override val delete: String = "Excluir",
    override val resetToOriginal: String = "Redefinir para original",
    override val cloneSpellName: String = "Nome da magia",
    override val save: String = "Salvar",
    override val deleteConfirmation: String = "Tem certeza que deseja excluir esta magia?",
    override val resetConfirmation: String = "Tem certeza que deseja redefinir esta magia para sua versão original?",
) : SpellDetailStrings

internal fun SpellDetailStrings.formatSubTitle(level: Int, school: String, ritual: Boolean): String {
    val subtitleCantrip = if (ritual) subtitleCantripWithRitual else subtitleCantrip
    val subtitleLevel = if (ritual) subtitleLevelWithRitual else subtitleLevel
    return if (level == 0) subtitleCantrip.format(school) else subtitleLevel.format(level, school)
}

internal fun SpellDetailStrings(): SpellDetailStrings = SpellDetailEnStrings()

internal data class SpellDetailEsStrings(
    override val subtitleCantrip: String = "Truco de magia, {0}",
    override val subtitleCantripWithRitual: String = "Truco de magia (Ritual), {0}",
    override val subtitleLevel: String = "Nivel {0}, {1}",
    override val subtitleLevelWithRitual: String = "Nivel {0} (Ritual), {1}",
    override val castingTime: String = "Tiempo de Lanzamiento:",
    override val range: String = "Alcance:",
    override val components: String = "Componentes:",
    override val duration: String = "Duración:",
    override val saveType: String = "Tirada de Salvación:",
    override val concentration: String = "Concentración",
    override val atHigherLevels: String = "En Niveles Superiores.",
    override val savingThrowStrength: String = "Fuerza",
    override val savingThrowDexterity: String = "Destreza",
    override val savingThrowConstitution: String = "Constitución",
    override val savingThrowIntelligence: String = "Inteligencia",
    override val savingThrowWisdom: String = "Sabiduría",
    override val savingThrowCharisma: String = "Carisma",
    override val schoolAbjuration: String = "abjuración",
    override val schoolConjuration: String = "conjuración",
    override val schoolDivination: String = "adivinación",
    override val schoolEnchantment: String = "encantamiento",
    override val schoolEvocation: String = "evocación",
    override val schoolIllusion: String = "ilusión",
    override val schoolNecromancy: String = "nigromancia",
    override val schoolTransmutation: String = "transmutación",
    override val cantrip: String = "Truco de magia",
    override val options: String = "Opciones",
    override val clone: String = "Clonar",
    override val edit: String = "Editar",
    override val delete: String = "Eliminar",
    override val resetToOriginal: String = "Restablecer al original",
    override val cloneSpellName: String = "Nombre de la magia",
    override val save: String = "Guardar",
    override val deleteConfirmation: String = "¿Estás seguro de que quieres eliminar esta magia?",
    override val resetConfirmation: String = "¿Estás seguro de que quieres restablecer esta magia a su versión original?",
) : SpellDetailStrings

internal fun AppLocalization.getStrings(): SpellDetailStrings {
    return when (getLanguage()) {
        Language.ENGLISH -> SpellDetailEnStrings()
        Language.PORTUGUESE -> SpellDetailPtStrings()
        Language.SPANISH -> SpellDetailEsStrings()
    }
}
