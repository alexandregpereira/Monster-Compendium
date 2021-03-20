package br.alexandregpereira.hunter.scripts

import br.alexandregpereira.hunter.data.di.remoteDataSourceModule
import br.alexandregpereira.hunter.data.monster.remote.MonsterRemoteDataSource
import br.alexandregpereira.hunter.data.monster.remote.model.*
import br.alexandregpereira.hunter.data.monster.remote.model.MonsterTypeDto
import br.alexandregpereira.hunter.dndapi.data.Monster
import br.alexandregpereira.hunter.dndapi.data.MonsterType
import br.alexandregpereira.hunter.dndapi.data.Proficiency
import br.alexandregpereira.hunter.scripts.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.serialization.decodeFromString
import okhttp3.*
import org.koin.core.component.inject
import java.io.IOException
import java.util.*

private const val GITHUB_IMAGE_HOST =
    "https://raw.githubusercontent.com/alexandregpereira/dnd-monster-manual/main/images"

@FlowPreview
@ExperimentalCoroutinesApi
suspend fun main() = start {
    val koinComponent = createKoinComponent(remoteDataSourceModule)
    val monsterRemoteDataSource by koinComponent.inject<MonsterRemoteDataSource>()

    val monsters = json.decodeFromString<List<Monster>>(readJsonFile(JSON_FILE_NAME))
        .asMonstersFormatted()
        .asSequence()
        .asFlow()
        .flatMapMerge {
            it.downloadImage()
        }
        .toList()
        .filterNotNull()
        .sortedBy { it.index }

    println("\n${monsters.size} monsters formatted")
    monsters.forEach { println("id: ${it.index}, name: ${it.name}") }

    monsterRemoteDataSource.insertMonsters(monsters).collect()
}

private fun List<Monster>.asMonstersFormatted(): List<MonsterDto> {
    return this.filter { it.type != MonsterType.OTHER }.map {
        MonsterDto(
            index = it.getId(),
            type = MonsterTypeDto.valueOf(it.type.name),
            name = it.name,
            subtitle = it.formatSubtitle(),
            imageUrl = it.getImageUrl(),
            size = it.size,
            alignment = it.alignment,
            subtype = it.subtype,
            armorClass = it.armorClass,
            hitPoints = it.hitPoints,
            hitDice = it.hitDice,
            speed = it.asSpeedFormatted(),
            abilityScores = it.asAbilityScoresFormatted(),
            savingThrows = it.getSavingThrows(),
            skills = it.getSkills(),
            damageVulnerabilities = it.damageVulnerabilities.getDamages(),
            damageResistances = it.damageResistances.getDamages(),
            damageImmunities = it.damageImmunities.getDamages(),
        )
    }
}

private fun Monster.formatSubtitle(): String {
    val subType = if (subtype.isNullOrEmpty()) "," else "($subtype),"
    return "$size ${type.name.toLowerCase(Locale.ROOT)}$subType $alignment"
}

private fun Monster.getImageUrl(): String {
    val imageName = getId()
    return "$GITHUB_IMAGE_HOST/$imageName.png"
}

private fun Monster.getId(): String {
    return if (isDragon()) {
        "dragon-$index"
    } else if (subtype.isNullOrEmpty() || subtypeBlackList.contains(subtype)) {
        index
    } else {
        "$subtype-$index"
    }
}

private fun Monster.isDragon(): Boolean {
    return this.index.endsWith("-dragon")
}

private fun Monster.asSpeedFormatted(): SpeedDto = speed.run {
    val burrow = createSpeedValue(SpeedTypeDto.BURROW, burrow)
    val climb = createSpeedValue(SpeedTypeDto.CLIMB, climb)
    val fly = createSpeedValue(SpeedTypeDto.FLY, fly)
    val walk = createSpeedValue(SpeedTypeDto.WALK, walk)
    val swim = createSpeedValue(SpeedTypeDto.SWIM, swim)
    return SpeedDto(
        hover = hover,
        values = listOfNotNull(burrow, climb, fly, walk, swim)
    )
}

private fun createSpeedValue(speedType: SpeedTypeDto, value: String?): SpeedValueDto? = value?.let {
    val distance = it.split(" ").first().toInt()
    SpeedValueDto(
        type = speedType,
        measurementUnit = MeasurementUnitDto.FEET,
        value = distance,
        valueFormatted = "$distance ${MeasurementUnitDto.FEET.value}"
    )
}

private fun Monster.asAbilityScoresFormatted(): List<AbilityScoreDto> {
    val strength = AbilityScoreDto(
        type = AbilityScoreTypeDto.STRENGTH,
        value = strength,
        modifier = calculateAbilityScoreModifier(strength)
    )
    val dexterity = AbilityScoreDto(
        type = AbilityScoreTypeDto.DEXTERITY,
        value = dexterity,
        modifier = calculateAbilityScoreModifier(dexterity)
    )
    val constitution = AbilityScoreDto(
        type = AbilityScoreTypeDto.CONSTITUTION,
        value = constitution,
        modifier = calculateAbilityScoreModifier(constitution)
    )
    val intelligence = AbilityScoreDto(
        type = AbilityScoreTypeDto.INTELLIGENCE,
        value = intelligence,
        modifier = calculateAbilityScoreModifier(intelligence)
    )
    val wisdom = AbilityScoreDto(
        type = AbilityScoreTypeDto.WISDOM,
        value = wisdom,
        modifier = calculateAbilityScoreModifier(wisdom)
    )
    val charisma = AbilityScoreDto(
        type = AbilityScoreTypeDto.CHARISMA,
        value = charisma,
        modifier = calculateAbilityScoreModifier(charisma)
    )
    return listOf(strength, dexterity, constitution, intelligence, wisdom, charisma)
}

private fun Monster.getSavingThrows(): List<SavingThrowDto> {
    return this.proficiencies.filter { it.proficiency.index.startsWith("saving-throw-") }.map {
        SavingThrowDto(
            type = it.proficiency.asSavingThrowType(),
            modifier = it.value
        )
    }
}

private fun Proficiency.asSavingThrowType(): AbilityScoreTypeDto {
    return when (val index = index.removePrefix("saving-throw-")) {
        "str" -> AbilityScoreTypeDto.STRENGTH
        "dex" -> AbilityScoreTypeDto.DEXTERITY
        "con" -> AbilityScoreTypeDto.CONSTITUTION
        "int" -> AbilityScoreTypeDto.INTELLIGENCE
        "wis" -> AbilityScoreTypeDto.WISDOM
        "cha" -> AbilityScoreTypeDto.CHARISMA
        else -> throw IllegalArgumentException("Invalid Saving throw: $index")
    }
}

private fun Monster.getSkills(): List<SkillDto> {
    return this.proficiencies.filter { it.proficiency.index.startsWith("skill-") }.map {
        SkillDto(
            index = it.proficiency.index.removePrefix("skill-"),
            modifier = it.value
        )
    }
}

private fun List<String>.getDamages(): List<DamageDto> {
    return this.map {
        val damageType = when {
            it.startsWith("acid") -> DamageTypeDto.ACID
            it.startsWith("bludgeoning") -> DamageTypeDto.BLUDGEONING
            it.startsWith("cold") -> DamageTypeDto.COLD
            it.startsWith("fire") -> DamageTypeDto.FIRE
            it.startsWith("lightning") -> DamageTypeDto.LIGHTNING
            it.startsWith("necrotic") -> DamageTypeDto.NECROTIC
            it.startsWith("piercing") -> DamageTypeDto.PIERCING
            it.startsWith("poison") -> DamageTypeDto.POISON
            it.startsWith("psychic") -> DamageTypeDto.PSYCHIC
            it.startsWith("radiant") -> DamageTypeDto.RADIANT
            it.startsWith("slashing") -> DamageTypeDto.SLASHING
            it.startsWith("thunder") -> DamageTypeDto.THUNDER
            else -> DamageTypeDto.OTHER
        }
        DamageDto(
            index = it.toLowerCase(Locale.ROOT),
            type = damageType,
            name = it.capitalize(Locale.ROOT)
        )
    }
}

private fun calculateAbilityScoreModifier(value: Int): Int {
    return when (value) {
        1 -> -5
        in 2..3 -> -4
        in 4..5 -> -3
        in 6..7 -> -2
        in 8..9 -> -1
        in 10..11 -> 0
        in 12..13 -> 1
        in 14..15 -> 2
        in 16..17 -> 3
        in 18..19 -> 4
        in 20..21 -> 5
        in 22..23 -> 6
        in 24..25 -> 7
        in 26..27 -> 8
        in 28..29 -> 9
        else -> 10
    }
}

@ExperimentalCoroutinesApi
private fun MonsterDto.downloadImage(): Flow<MonsterDto?> = callbackFlow {
    val client = OkHttpClient()

    val request: Request = Request.Builder()
        .url(imageUrl)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            println("request failed: $index: " + e.message)
            channel.offer(null)
            channel.close()
        }

        @Throws(IOException::class)
        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                println("request success: $index")
                channel.offer(element = this@downloadImage)

            } else {
                println("request failed: $index")
                channel.offer(element = null)
            }
            channel.close()
        }
    })

    awaitClose()
}

private val subtypeBlackList = listOf(
    "any race",
    "dwarf",
    "elf",
    "gnoll",
    "gnome",
    "goblinoid",
    "grimlock",
    "human",
    "kobold",
    "sahuagin",
    "shapechanger",
    "titan",
)