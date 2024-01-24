package br.alexandregpereira.hunter.spell.compendium

data class SpellCompendiumState(
    val isShowing: Boolean = false,
    val spellsGroupByLevel: Map<String, List<SpellCompendiumItemState>> = emptyMap(),
    val searchText: String = "",
    val searchTextLabel: String = "",
    val initialItemIndex: Int = 0,
)

data class SpellCompendiumItemState(
    val index: String,
    val name: String,
    val school: SpellCompendiumSchoolOfMagicState,
    val selected: Boolean = false,
)

enum class SpellCompendiumSchoolOfMagicState {
    ABJURATION,
    CONJURATION,
    DIVINATION,
    ENCHANTMENT,
    EVOCATION,
    ILLUSION,
    NECROMANCY,
    TRANSMUTATION,
}
