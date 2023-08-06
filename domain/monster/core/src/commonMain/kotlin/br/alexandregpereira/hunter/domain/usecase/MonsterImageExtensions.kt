package br.alexandregpereira.hunter.domain.usecase

import br.alexandregpereira.hunter.domain.model.Color
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.MonsterImage
import br.alexandregpereira.hunter.domain.model.MonsterImageData

fun List<Monster>.appendMonsterImages(
    monsterImages: List<MonsterImage>
): List<Monster> = map { monster ->
    val monsterImage = monsterImages.firstOrNull { monsterImage ->
        monsterImage.monsterIndex == monster.index
    } ?: MonsterImage(
        monsterIndex = monster.index,
        backgroundColor = Color(light = "#e0dfd1", dark = "#e0dfd1"),
        isHorizontalImage = false,
        imageUrl = DEFAULT_IMAGE_BASE_URL + "default-${monster.type.name.lowercase()}.png"
    )

    monster.copy(
        imageData = MonsterImageData(
            url = monsterImage.imageUrl,
            backgroundColor = monsterImage.backgroundColor,
            isHorizontal = monsterImage.isHorizontalImage
        )
    )
}

private const val DEFAULT_IMAGE_BASE_URL =
    "https://raw.githubusercontent.com/alexandregpereira/hunter-api/main/images/"