package br.alexandregpereira.beholder.dndapi.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
enum class MonsterType {
    @SerialName("aberration")
    ABERRATION,
    @SerialName("beast")
    BEAST,
    @SerialName("celestial")
    CELESTIAL,
    @SerialName("construct")
    CONSTRUCT,
    @SerialName("dragon")
    DRAGON,
    @SerialName("elemental")
    ELEMENTAL,
    @SerialName("fey")
    FEY,
    @SerialName("fiend")
    FIEND,
    @SerialName("giant")
    GIANT,
    @SerialName("humanoid")
    HUMANOID,
    @SerialName("monstrosity")
    MONSTROSITY,
    @SerialName("ooze")
    OOZE,
    @SerialName("plant")
    PLANT,
    @SerialName("undead")
    UNDEAD
}