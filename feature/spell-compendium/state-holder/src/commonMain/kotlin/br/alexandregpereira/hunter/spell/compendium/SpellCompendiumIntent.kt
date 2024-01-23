package br.alexandregpereira.hunter.spell.compendium

interface SpellCompendiumIntent {
    fun onSearchTextChange(text: String)

    fun onSpellClick(spellIndex: String)

    fun onSpellLongClick(spellIndex: String)

    fun onClose()
}

class EmptySpellCompendiumIntent : SpellCompendiumIntent {

    override fun onSearchTextChange(text: String) {}

    override fun onSpellClick(spellIndex: String) {}

    override fun onSpellLongClick(spellIndex: String) {}

    override fun onClose() {}
}
