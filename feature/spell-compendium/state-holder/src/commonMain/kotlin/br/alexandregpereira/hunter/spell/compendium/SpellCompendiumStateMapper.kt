package br.alexandregpereira.hunter.spell.compendium

import br.alexandregpereira.hunter.domain.spell.model.Spell
import br.alexandregpereira.hunter.domain.spell.model.SchoolOfMagic.ABJURATION as ABJURATION_DOMAIN
import br.alexandregpereira.hunter.domain.spell.model.SchoolOfMagic.CONJURATION as CONJURATION_DOMAIN
import br.alexandregpereira.hunter.domain.spell.model.SchoolOfMagic.DIVINATION as DIVINATION_DOMAIN
import br.alexandregpereira.hunter.domain.spell.model.SchoolOfMagic.ENCHANTMENT as ENCHANTMENT_DOMAIN
import br.alexandregpereira.hunter.domain.spell.model.SchoolOfMagic.EVOCATION as EVOCATION_DOMAIN
import br.alexandregpereira.hunter.domain.spell.model.SchoolOfMagic.ILLUSION as ILLUSION_DOMAIN
import br.alexandregpereira.hunter.domain.spell.model.SchoolOfMagic.NECROMANCY as NECROMANCY_DOMAIN
import br.alexandregpereira.hunter.domain.spell.model.SchoolOfMagic.TRANSMUTATION as TRANSMUTATION_DOMAIN

internal fun List<Spell>.asState(
    selectedSpellIndexes: List<String> = emptyList(),
): List<SpellCompendiumItemState> {
    val allIndexes = map { spellIndex ->
        spellIndex.index.takeIf { selectedSpellIndexes.contains(it) }
    }
    return mapIndexed { i, spell ->
        spell.asState(selected = allIndexes[i] != null)
    }
}

internal fun Spell.asState(selected: Boolean): SpellCompendiumItemState {
    return SpellCompendiumItemState(
        index = index,
        name = name,
        school = when (school) {
            ABJURATION_DOMAIN -> SpellCompendiumSchoolOfMagicState.ABJURATION
            CONJURATION_DOMAIN -> SpellCompendiumSchoolOfMagicState.CONJURATION
            DIVINATION_DOMAIN -> SpellCompendiumSchoolOfMagicState.DIVINATION
            ENCHANTMENT_DOMAIN -> SpellCompendiumSchoolOfMagicState.ENCHANTMENT
            EVOCATION_DOMAIN -> SpellCompendiumSchoolOfMagicState.EVOCATION
            ILLUSION_DOMAIN -> SpellCompendiumSchoolOfMagicState.ILLUSION
            NECROMANCY_DOMAIN -> SpellCompendiumSchoolOfMagicState.NECROMANCY
            TRANSMUTATION_DOMAIN -> SpellCompendiumSchoolOfMagicState.TRANSMUTATION
        },
        selected = selected,
    )
}
