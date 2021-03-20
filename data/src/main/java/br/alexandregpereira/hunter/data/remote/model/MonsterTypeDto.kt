package br.alexandregpereira.hunter.data.remote.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
enum class MonsterTypeDto {
    @SerialName("ABERRATION")
    ABERRATION,
    @SerialName("BEAST")
    BEAST,
    @SerialName("CELESTIAL")
    CELESTIAL,
    @SerialName("CONSTRUCT")
    CONSTRUCT,
    @SerialName("DRAGON")
    DRAGON,
    @SerialName("ELEMENTAL")
    ELEMENTAL,
    @SerialName("FEY")
    FEY,
    @SerialName("FIEND")
    FIEND,
    @SerialName("GIANT")
    GIANT,
    @SerialName("HUMANOID")
    HUMANOID,
    @SerialName("MONSTROSITY")
    MONSTROSITY,
    @SerialName("OOZE")
    OOZE,
    @SerialName("PLANT")
    PLANT,
    @SerialName("UNDEAD")
    UNDEAD
}