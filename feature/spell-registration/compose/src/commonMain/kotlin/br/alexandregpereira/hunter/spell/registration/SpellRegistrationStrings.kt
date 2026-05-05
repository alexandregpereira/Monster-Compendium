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

package br.alexandregpereira.hunter.spell.registration

import br.alexandregpereira.hunter.localization.Language

data class SpellRegistrationStrings(
    val addSpell: String = "Add Spell",
    val editSpell: String = "Edit Spell",
    val save: String = "Save",
    val name: String = "Name",
    val level: String = "Level",
    val castingTime: String = "Casting Time",
    val components: String = "Components",
    val duration: String = "Duration",
    val range: String = "Range",
    val ritual: String = "Ritual",
    val concentration: String = "Concentration",
    val savingThrowType: String = "Saving Throw",
    val none: String = "None",
    val damageType: String = "Damage Type",
    val school: String = "School of Magic",
    val description: String = "Description",
    val higherLevel: String = "At Higher Levels",
    val schoolAbjuration: String = "Abjuration",
    val schoolConjuration: String = "Conjuration",
    val schoolDivination: String = "Divination",
    val schoolEnchantment: String = "Enchantment",
    val schoolEvocation: String = "Evocation",
    val schoolIllusion: String = "Illusion",
    val schoolNecromancy: String = "Necromancy",
    val schoolTransmutation: String = "Transmutation",
    val savingThrowStrength: String = "Strength",
    val savingThrowDexterity: String = "Dexterity",
    val savingThrowConstitution: String = "Constitution",
    val savingThrowIntelligence: String = "Intelligence",
    val savingThrowWisdom: String = "Wisdom",
    val savingThrowCharisma: String = "Charisma",
)

internal fun getSpellRegistrationStrings(lang: Language): SpellRegistrationStrings {
    return when (lang) {
        Language.ENGLISH -> SpellRegistrationStrings()
        Language.PORTUGUESE -> SpellRegistrationStrings(
            addSpell = "Adicionar Magia",
            editSpell = "Editar Magia",
            save = "Salvar",
            name = "Nome",
            level = "Nível",
            castingTime = "Tempo de Conjuração",
            components = "Componentes",
            duration = "Duração",
            range = "Alcance",
            ritual = "Ritual",
            concentration = "Concentração",
            savingThrowType = "Teste de Resistência",
            none = "Nenhum",
            damageType = "Tipo de Dano",
            school = "Escola de Magia",
            description = "Descrição",
            higherLevel = "Em Níveis Superiores",
            schoolAbjuration = "Abjuração",
            schoolConjuration = "Conjuração",
            schoolDivination = "Adivinhação",
            schoolEnchantment = "Encantamento",
            schoolEvocation = "Evocação",
            schoolIllusion = "Ilusão",
            schoolNecromancy = "Necromancia",
            schoolTransmutation = "Transmutação",
            savingThrowStrength = "Força",
            savingThrowDexterity = "Destreza",
            savingThrowConstitution = "Constituição",
            savingThrowIntelligence = "Inteligência",
            savingThrowWisdom = "Sabedoria",
            savingThrowCharisma = "Carisma",
        )
        Language.SPANISH -> SpellRegistrationStrings(
            addSpell = "Añadir Magia",
            editSpell = "Editar Magia",
            save = "Guardar",
            name = "Nombre",
            level = "Nivel",
            castingTime = "Tiempo de Lanzamiento",
            components = "Componentes",
            duration = "Duración",
            range = "Alcance",
            ritual = "Ritual",
            concentration = "Concentración",
            savingThrowType = "Tirada de Salvación",
            none = "Ninguno",
            damageType = "Tipo de Daño",
            school = "Escuela de Magia",
            description = "Descripción",
            higherLevel = "A Niveles Superiores",
            schoolAbjuration = "Abjuración",
            schoolConjuration = "Conjuración",
            schoolDivination = "Adivinación",
            schoolEnchantment = "Encantamiento",
            schoolEvocation = "Evocación",
            schoolIllusion = "Ilusión",
            schoolNecromancy = "Nigromancia",
            schoolTransmutation = "Transmutación",
            savingThrowStrength = "Fuerza",
            savingThrowDexterity = "Destreza",
            savingThrowConstitution = "Constitución",
            savingThrowIntelligence = "Inteligencia",
            savingThrowWisdom = "Sabiduría",
            savingThrowCharisma = "Carisma",
        )
    }
}
