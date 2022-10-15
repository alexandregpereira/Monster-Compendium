package br.alexandregpereira.hunter.domain.monster.spell.model

data class Spellcasting(
    val description: String,
    val type: SpellcastingType,
    val usages: List<SpellUsage>
)
