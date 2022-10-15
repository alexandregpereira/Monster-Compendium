package br.alexandregpereira.hunter.data.monster.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MonsterImageDto(
    @SerialName("monster_index")
    val monsterIndex: String,
    @SerialName("background_color")
    val backgroundColor: ColorDto = ColorDto(),
    @SerialName("is_horizontal_image")
    val isHorizontalImage: Boolean,
    @SerialName("image_url")
    val imageUrl: String,
)
