package br.alexandregpereira.hunter.domain.model.factory

import br.alexandregpereira.hunter.domain.model.ChallengeRating
import br.alexandregpereira.hunter.domain.model.Color
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.MonsterImageData
import br.alexandregpereira.hunter.domain.model.MonsterStatus
import br.alexandregpereira.hunter.domain.model.MonsterType
import br.alexandregpereira.hunter.domain.model.Speed
import br.alexandregpereira.hunter.domain.model.Stats

object MonsterFactory {

    fun createEmpty(index: String): Monster {
        val imageData = MonsterImageData(
            url = "",
            backgroundColor = Color(light = "", dark = ""),
            isHorizontal = false,
            contentScale = null,
            isImageDataFromCustomDatabase = false,
        )
        return Monster(
            index = index,
            name = "",
            type = MonsterType.ABERRATION,
            challengeRatingData = ChallengeRating.create(0.0f),
            imageData = imageData,
            subtype = null,
            group = null,
            subtitle = "",
            size = "",
            alignment = "",
            stats = Stats(
                armorClass = 0,
                hitPoints = 0,
                hitDice = ""
            ),
            senses = emptyList(),
            languages = "",
            sourceName = "",
            speed = Speed(
                hover = false,
                values = emptyList(),
            ),
            abilityScores = emptyList(),
            savingThrows = emptyList(),
            skills = emptyList(),
            damageVulnerabilities = emptyList(),
            damageResistances = emptyList(),
            damageImmunities = emptyList(),
            conditionImmunities = emptyList(),
            specialAbilities = emptyList(),
            actions = emptyList(),
            bonusActions = emptyList(),
            legendaryActions = emptyList(),
            reactions = emptyList(),
            spellcastings = emptyList(),
            lore = null,
            status = MonsterStatus.Original,
            originalImageData = imageData,
            customMonsterImage = null,
        )
    }
}