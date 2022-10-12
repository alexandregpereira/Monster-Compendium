package br.alexandregpereira.hunter.spell.detail.ui

import br.alexandregpereira.hunter.ui.compose.SchoolOfMagicState

data class SpellState(
    val index: String,
    val name: String,
    val level: Int,
    val castingTime: String,
    val components: String,
    val duration: String,
    val range: String,
    val ritual: Boolean,
    val concentration: Boolean,
    val savingThrowType: SavingThrowTypeState?,
    val school: SchoolOfMagicState,
    val description: String,
    val higherLevel: String?,
    val damageType: String? = null,
)

enum class SavingThrowTypeState {
    STRENGTH,
    DEXTERITY,
    CONSTITUTION,
    INTELLIGENCE,
    WISDOM,
    CHARISMA
}
