package br.alexandregpereira.hunter.domain.monster.spell.model

data class SpellPreview(
    val index: String,
    val name: String,
    val level: Int,
    val school: SchoolOfMagic,
)

enum class SchoolOfMagic {
    ABJURATION,
    CONJURATION,
    DIVINATION,
    ENCHANTMENT,
    EVOCATION,
    ILLUSION,
    NECROMANCY,
    TRANSMUTATION,
}
