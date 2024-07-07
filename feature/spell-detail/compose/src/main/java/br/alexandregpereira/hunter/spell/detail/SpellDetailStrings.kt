package br.alexandregpereira.hunter.spell.detail

import br.alexandregpereira.hunter.localization.AppLocalization
import br.alexandregpereira.hunter.localization.Language

interface SpellDetailStrings {
    val subtitle: (Int, String) -> String
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
}

internal data class SpellDetailEnStrings(
    override val subtitle: (Int, String) -> String = { level, school ->
        if (level == 0) "Cantrip, $school" else "Level $level, $school"
    },
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
) : SpellDetailStrings

internal data class SpellDetailPtStrings(
    override val subtitle: (Int, String) -> String = { level, school ->
        if (level == 0) "Truque, $school" else "$level° círculo, $school"
    },
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
) : SpellDetailStrings

fun SpellDetailStrings(): SpellDetailStrings = SpellDetailEnStrings()

internal fun AppLocalization.getStrings(): SpellDetailStrings {
    return when (getLanguage()) {
        Language.ENGLISH -> SpellDetailEnStrings()
        Language.PORTUGUESE -> SpellDetailPtStrings()
    }
}
