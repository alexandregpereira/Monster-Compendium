package br.alexandregpereira.hunter.domain.monster.spell.model

data class SpellUsage(
    val group: String,
    val spells: List<SpellPreview>
)
