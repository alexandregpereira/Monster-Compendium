package br.alexandregpereira.hunter.monster.registration.domain

import br.alexandregpereira.hunter.domain.model.AbilityScore
import br.alexandregpereira.hunter.domain.model.AbilityScoreType
import br.alexandregpereira.hunter.domain.model.ChallengeRating
import br.alexandregpereira.hunter.domain.model.Color
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.MonsterImageData
import br.alexandregpereira.hunter.domain.model.MonsterStatus
import br.alexandregpereira.hunter.domain.model.factory.MonsterFactory
import br.alexandregpereira.hunter.uuid.generateUUID

internal fun interface GenerateNewMonster {
    operator fun invoke(): Monster
}

internal class GenerateNewMonsterUseCase : GenerateNewMonster {
    override fun invoke(): Monster {
        val imageData = createImageData()
        return MonsterFactory.createEmpty(
            index = "monster-${generateUUID()}"
        ).copy(
            status = MonsterStatus.Created,
            abilityScores = AbilityScoreType.entries.map {
                AbilityScore(
                    type = it,
                    value = 10,
                    modifier = 0
                )
            },
            imageData = imageData,
            originalImageData = imageData,
            challengeRatingData = ChallengeRating.create(.5f),
            sourceName = "Homebrew",
        )
    }

    private fun createImageData(): MonsterImageData {
        return MonsterImageData(
            backgroundColor = Color.createDefault(),
            isHorizontal = false,
            url = "",
            contentScale = null,
            isImageDataFromCustomDatabase = false,
        )
    }
}
