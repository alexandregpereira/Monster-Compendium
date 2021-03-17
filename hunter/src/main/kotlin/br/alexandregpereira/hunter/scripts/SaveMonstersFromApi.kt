package br.alexandregpereira.hunter.scripts

import br.alexandregpereira.hunter.dndapi.data.Monster
import br.alexandregpereira.hunter.dndapi.data.MonsterApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.serialization.ExperimentalSerializationApi
import java.lang.Exception

@ExperimentalSerializationApi
val monsterApi: MonsterApi = retrofit.create(MonsterApi::class.java)

@FlowPreview
@ExperimentalSerializationApi
suspend fun main() = start {
    val monsterResponse = monsterApi.getMonsters()

    val monsters = monsterResponse.results.asFlow()
        .flatMapMerge {
            getMonsterFlow(it.index)
        }
        .toList()
        .sortedBy { it.name }

    saveJsonFile(monsters, JSON_FILE_NAME)
}

private fun Monster.formatDamages(): Monster {
    val damageImmunities = this.damageImmunities.map {
        it.replace("damage ", "").splitDamage()
    }.reduceOrNull { acc, list -> acc + list }
    val damageResistances = this.damageResistances.map {
        it.replace("damage ", "").splitDamage()
    }.reduceOrNull { acc, list -> acc + list }
    val damageVulnerabilities = this.damageVulnerabilities.map {
        it.replace("damage ", "").splitDamage()
    }.reduceOrNull { acc, list -> acc + list }
    return this.copy(
        damageImmunities = damageImmunities ?: emptyList(),
        damageResistances = damageResistances ?: emptyList(),
        damageVulnerabilities = damageVulnerabilities ?: emptyList(),
    )
}

private fun String.splitDamage(): List<String> {
    return when (this) {
        "bludgeoning, piercing, and slashing from nonmagical weapons" -> {
            listOf("bludgeoning*", "piercing*", "slashing*") +
                    listOf("*bludgeoning, piercing, and slashing from nonmagical weapons")
        }
        "bludgeoning, piercing, and slashing from nonmagical weapons that aren't adamantine" -> {
            listOf("bludgeoning*", "piercing*", "slashing*") +
                    listOf("*bludgeoning, piercing, and slashing from nonmagical weapons that aren't adamantine")
        }
        "bludgeoning, piercing, and slashing from nonmagical attacks not made with silvered weapons",
        "bludgeoning, piercing, and slashing from nonmagical weapons that aren't silvered",
        "bludgeoning, piercing, and slashing from nonmagical/nonsilver weapons" -> {
            listOf("bludgeoning*", "piercing*", "slashing*") +
                    listOf("*bludgeoning, piercing, and slashing from nonmagical weapons that aren't silvered")
        }
        "from spells" -> {
            listOf(
                "acid*",
                "cold*",
                "fire*",
                "lightning*",
                "necrotic*",
                "poison*",
                "psychic*",
                "radiant*",
                "thunder*",
                "*from spells"
            )
        }
        "non magical bludgeoning, piercing, and slashing (from stoneskin)" -> {
            listOf(
                "bludgeoning*",
                "piercing*",
                "slashing*",
                "*non magical bludgeoning, piercing, and slashing (from stoneskin)"
            )
        }
        "piercing and slashing from nonmagical weapons that aren't adamantine" -> {
            listOf("piercing*", "slashing*") +
                    listOf("*piercing and slashing from nonmagical weapons that aren't adamantine")
        }
        "piercing from magic weapons wielded by good creatures" -> {
            listOf("piercing*") +
                    listOf("*piercing from magic weapons wielded by good creatures")
        }
        else -> listOf(this)
    }
}

@ExperimentalSerializationApi
private suspend fun getMonsterFlow(index: String): Flow<Monster> = flow {
    runCatching {
        getMonster(index).formatDamages()
    }.onFailure {
        throw Exception("Error: $index", it)
    }.onSuccess {
        emit(it)
    }
}

@ExperimentalSerializationApi
private suspend fun getMonster(index: String): Monster {
    println("Monster: $index")
    return monsterApi.getMonster(index)
}
