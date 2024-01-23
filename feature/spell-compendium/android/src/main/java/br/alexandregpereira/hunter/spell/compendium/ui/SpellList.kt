package br.alexandregpereira.hunter.spell.compendium.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.spell.compendium.SpellCompendiumIntent
import br.alexandregpereira.hunter.spell.compendium.SpellCompendiumItemState
import br.alexandregpereira.hunter.ui.compendium.Compendium
import br.alexandregpereira.hunter.ui.compendium.CompendiumColumns
import br.alexandregpereira.hunter.ui.compendium.CompendiumItemState
import br.alexandregpereira.hunter.ui.compose.SchoolOfMagicState
import br.alexandregpereira.hunter.ui.compose.SpellIconInfo
import br.alexandregpereira.hunter.ui.compose.SpellIconSize

@Composable
internal fun SpellList(
    spellsGroupByLevel: Map<String, List<SpellCompendiumItemState>>,
    initialItemIndex: Int,
    intent: SpellCompendiumIntent,
) {
    val items = remember(spellsGroupByLevel) { spellsGroupByLevel.toCompendiumItems() }
    val listState = rememberLazyGridState(initialFirstVisibleItemIndex = initialItemIndex)
    Compendium(
        items = items,
        animateItems = true,
        listState = listState,
        columns = CompendiumColumns.Adaptive(minSize = SpellIconSize.SMALL.value + 16),
    ) { item ->
        val spell = item.value as SpellCompendiumItemState
        val alpha = if (spell.selected) 0.5f else 1f
        SpellIconInfo(
            name = spell.name,
            school = SchoolOfMagicState.valueOf(spell.school.name),
            size = SpellIconSize.SMALL,
            modifier = Modifier.padding(bottom = 16.dp).alpha(alpha),
            onClick = { intent.onSpellClick(spell.index) },
            onLongClick = { intent.onSpellLongClick(spell.index) },
        )
    }

    LaunchedEffect(initialItemIndex) {
        if (listState.firstVisibleItemIndex != initialItemIndex) {
            listState.scrollToItem(initialItemIndex)
        }
    }
}

private fun Map<String, List<SpellCompendiumItemState>>.toCompendiumItems(): List<CompendiumItemState> {
    val result = mutableListOf<CompendiumItemState>()
    entries.forEach { (level, spells) ->
        result.add(CompendiumItemState.Title(level))
        result.addAll(spells.toCompendiumItems())
    }

    return result
}

private fun List<SpellCompendiumItemState>.toCompendiumItems(): List<CompendiumItemState> {
    return map { CompendiumItemState.Item(it) }
}
