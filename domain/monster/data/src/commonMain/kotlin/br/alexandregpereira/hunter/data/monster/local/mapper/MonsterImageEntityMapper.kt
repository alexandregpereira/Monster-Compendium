package br.alexandregpereira.hunter.data.monster.local.mapper

import br.alexandregpereira.hunter.data.monster.local.entity.MonsterImageEntity
import br.alexandregpereira.hunter.domain.model.Color
import br.alexandregpereira.hunter.domain.model.MonsterImage
import br.alexandregpereira.hunter.domain.model.MonsterImageContentScale
import br.alexandregpereira.hunter.domain.model.MonsterImageData

internal fun MonsterImage.toMonsterImageEntity(): MonsterImageEntity {
    return createMonsterImageEntity(
        monsterIndex = monsterIndex,
        imageUrl = imageUrl,
        backgroundColor = backgroundColor,
        isHorizontalImage = isHorizontalImage,
        imageContentScale = contentScale,
    )
}

internal fun MonsterImageData.toMonsterImageEntity(monsterIndex: String): MonsterImageEntity {
    return createMonsterImageEntity(
        monsterIndex = monsterIndex,
        imageUrl = url,
        backgroundColor = backgroundColor,
        isHorizontalImage = isHorizontal,
        imageContentScale = contentScale,
    )
}

internal fun createMonsterImageEntity(
    monsterIndex: String,
    imageUrl: String?,
    backgroundColor: Color?,
    isHorizontalImage: Boolean?,
    imageContentScale: MonsterImageContentScale?,
): MonsterImageEntity {
    return MonsterImageEntity(
        monsterIndex = monsterIndex,
        imageUrl = imageUrl,
        backgroundColorLight = backgroundColor?.light,
        backgroundColorDark = backgroundColor?.dark,
        isHorizontalImage = isHorizontalImage,
        imageContentScale = imageContentScale?.toInt(),
    )
}

internal fun MonsterImageEntity.toMonsterImage(): MonsterImage {
    return MonsterImage(
        monsterIndex = monsterIndex,
        imageUrl = imageUrl,
        backgroundColor = createColor(
            light = backgroundColorLight,
            dark = backgroundColorDark,
        ),
        isHorizontalImage = isHorizontalImage,
        contentScale = imageContentScale?.toMonsterImageContentScale(),
    )
}

internal fun MonsterImageEntity.toMonsterImageData(
    isImageDataFromCustomDatabase: Boolean,
): MonsterImageData {
    return MonsterImageData(
        url = imageUrl.orEmpty(),
        backgroundColor = Color(
            light = backgroundColorLight.orEmpty(),
            dark = backgroundColorDark.orEmpty(),
        ),
        isHorizontal = isHorizontalImage ?: false,
        contentScale = imageContentScale?.toMonsterImageContentScale(),
        isImageDataFromCustomDatabase = isImageDataFromCustomDatabase,
    )
}

internal fun createColor(
    light: String?,
    dark: String?,
): Color? {
    return if (light != null && dark != null) {
        Color(
            light = light,
            dark = dark
        )
    } else {
        null
    }
}

internal fun MonsterImageContentScale.toInt(): Int {
    return when (this) {
        MonsterImageContentScale.Fit -> 0
        MonsterImageContentScale.Crop -> 1
    }
}

internal fun Int.toMonsterImageContentScale(): MonsterImageContentScale? {
    return when (this) {
        0 -> MonsterImageContentScale.Fit
        1 -> MonsterImageContentScale.Crop
        else -> null
    }
}
