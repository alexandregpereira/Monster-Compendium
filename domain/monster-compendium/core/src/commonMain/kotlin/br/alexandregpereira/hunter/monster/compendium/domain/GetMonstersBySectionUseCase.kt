package br.alexandregpereira.hunter.monster.compendium.domain

import br.alexandregpereira.hunter.domain.collections.equalsWithNoSpecialChar
import br.alexandregpereira.hunter.domain.collections.removeSpecialCharacters
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.monster.compendium.domain.model.MonsterCompendiumItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class GetMonstersBySectionUseCase internal constructor() {

    operator fun invoke(monstersFlow: Flow<List<Monster>>): Flow<List<MonsterCompendiumItem>> {
        return monstersFlow
            .onEach {
                if (it.isEmpty()) throw MonsterCompendiumError.NoMonsterError()
            }
            .toMonsterCompendiumItems()
    }

    private fun String.getFirstLetter(): String {
        return this.first().uppercaseChar().toString().removeSpecialCharacters()
    }

    private fun Flow<List<Monster>>.toMonsterCompendiumItems(): Flow<List<MonsterCompendiumItem>> {
        return map { monsters ->
            val items = mutableListOf<MonsterCompendiumItem>()
            monsters.forEach { monster ->
                val title = monster.group?.takeUnless { it.isBlank() } ?: monster.name.getFirstLetter()
                val lastTitle = items.lastOrNull { it is MonsterCompendiumItem.Title }?.let { it as MonsterCompendiumItem.Title }?.value
                if (title.equalsWithNoSpecialChar(lastTitle).not()) {
                    val titleFirstLetter = title.getFirstLetter()
                    if (titleFirstLetter.equalsWithNoSpecialChar(lastTitle?.getFirstLetter()).not()) {
                        items.add(
                            MonsterCompendiumItem.Title(
                                id = titleFirstLetter + items.count {
                                    it is MonsterCompendiumItem.Title &&
                                            it.value.equalsWithNoSpecialChar(titleFirstLetter)
                                },
                                value = titleFirstLetter,
                                isHeader = true
                            )
                        )
                    }

                    val lastItem = items.lastOrNull()
                    if (lastItem !is MonsterCompendiumItem.Title || lastItem.value.equalsWithNoSpecialChar(title).not()) {
                        items.add(
                            MonsterCompendiumItem.Title(
                                id = title + items.count {
                                    it is MonsterCompendiumItem.Title && it.value.equalsWithNoSpecialChar(
                                        title
                                    )
                                },
                                value = title,
                                isHeader = items.find {
                                    it is MonsterCompendiumItem.Title && it.value
                                        .equalsWithNoSpecialChar(titleFirstLetter)
                                } == null
                            )
                        )
                    }
                }

                items.add(
                    MonsterCompendiumItem.Item(monster = monster)
                )
            }
            items
        }
    }
}
