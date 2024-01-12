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

    private fun List<Monster>.appendIsHorizontal(): List<Monster> {
        val monsterRowsMap: LinkedHashMap<Monster, Monster?> = linkedMapOf()
        var lastMonsterHorizontalIndex = -1
        var mod = 0
        val totalMonsters = this.size
        this.mapIndexed { index, monster ->
            if ((index + mod) % 2 == 0) {
                if (monster.imageData.isHorizontal &&
                    isIndexEligibleToBeHorizontal(index, lastMonsterHorizontalIndex, totalMonsters)
                ) {
                    lastMonsterHorizontalIndex = index
                    ++mod
                }
                monsterRowsMap[monster] = null
            } else {
                val lastIndex = index - 1
                val lastMonster = this[lastIndex]
                monsterRowsMap[lastMonster] = monster
            }
        }
        return this.map { monster ->
            val isHorizontal =
                monsterRowsMap.containsKey(monster) && monsterRowsMap[monster] == null
            monster.changeIsHorizontalImage(isHorizontal = isHorizontal)
        }
    }

    private fun isIndexEligibleToBeHorizontal(
        currentIndex: Int,
        lastMonsterHorizontalIndex: Int,
        totalMonsters: Int
    ): Boolean {
        return (lastMonsterHorizontalIndex == -1 && currentIndex < (totalMonsters - 2)) ||
                ((currentIndex - lastMonsterHorizontalIndex) >= HORIZONTAL_IMAGE_INTERVAL &&
                        currentIndex < (totalMonsters - 2))
    }

    private fun Monster.changeIsHorizontalImage(isHorizontal: Boolean): Monster {
        return copy(imageData = imageData.copy(isHorizontal = isHorizontal))
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
            items.appendIsMonsterImageHorizontal()
        }
    }

    private fun List<MonsterCompendiumItem>.appendIsMonsterImageHorizontal(): List<MonsterCompendiumItem> {
        val monsters = mutableListOf<Monster>()
        val monstersBySection = mutableListOf<List<Monster>>()
        val lastIndex = lastIndex
        forEachIndexed { i, item ->
            if (item is MonsterCompendiumItem.Item) {
                monsters.add(item.monster)
            }

            if ((item is MonsterCompendiumItem.Title || lastIndex == i) && monsters.isNotEmpty()) {
                monstersBySection.add(monsters.appendIsHorizontal())
                monsters.clear()
            }
        }

        val newMonsters = monstersBySection
            .takeIf { it.isNotEmpty() }
            ?.reduce { acc, value -> acc + value }
            .orEmpty()

        return map { item ->
            when (item) {
                is MonsterCompendiumItem.Title -> item
                is MonsterCompendiumItem.Item -> item.copy(
                    monster = newMonsters.firstOrNull {
                        it.index == item.monster.index
                    } ?: throw IllegalArgumentException("appendIsMonsterImageHorizontal error")
                )
            }
        }
    }
}

private const val HORIZONTAL_IMAGE_INTERVAL = 2