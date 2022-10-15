package br.alexandregpereira.hunter.domain.spell.model

data class Spell(
    val index: String,
    val name: String,
    val level: Int,
    val castingTime: String,
    val components: String,
    val duration: String,
    val range: String,
    val ritual: Boolean,
    val concentration: Boolean,
    val savingThrowType: SavingThrowType?,
    val damageType: String?,
    val school: SchoolOfMagic,
    val description: String,
    val higherLevel: String?
)
