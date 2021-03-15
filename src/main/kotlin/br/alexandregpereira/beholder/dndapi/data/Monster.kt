package br.alexandregpereira.beholder.dndapi.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Monster(
    @SerialName("index")
    val index: String,
    @SerialName("name")
    val name: String,
    @SerialName("size")
    val size: String,
    @SerialName("type")
    val type: MonsterType,
    @SerialName("subtype")
    val subtype: String?,
    @SerialName("alignment")
    val alignment: String,
    @SerialName("armor_class")
    val armorClass: Int,
    @SerialName("hit_points")
    val hitPoints: Int,
    @SerialName("hit_dice")
    val hitDice: String,
    @SerialName("speed")
    val speed: Speed,
    @SerialName("strength")
    val strength: Int,
    @SerialName("dexterity")
    val dexterity: Int,
    @SerialName("constitution")
    val constitution: Int,
    @SerialName("intelligence")
    val intelligence: Int,
    @SerialName("wisdom")
    val wisdom: Int,
    @SerialName("charisma")
    val charisma: Int,
    @SerialName("proficiencies")
    val proficiencies: List<ProficiencyValue>,
    @SerialName("damage_vulnerabilities")
    val damageVulnerabilities: List<String>,
    @SerialName("damage_resistances")
    val damageResistances: List<String>,
    @SerialName("damage_immunities")
    val damageImmunities: List<String>,
    @SerialName("condition_immunities")
    val conditionImmunities: List<APIReference>,
    @SerialName("senses")
    val senses: Senses,
    @SerialName("languages")
    val languages: String,
    @SerialName("challenge_rating")
    val challengeRating: Float,
    @SerialName("xp")
    val xp: Int,
    @SerialName("special_abilities")
    val specialAbilities: List<SpecialAbility> = emptyList(),
    @SerialName("actions")
    val actions: List<Action> = emptyList(),
    @SerialName("legendary_actions")
    val legendaryActions: List<LegendaryAction> = emptyList(),
    @SerialName("url")
    val url: String
)

@Serializable
data class Action(
    @SerialName("attack_bonus")
    val attackBonus: Int? = null,
    @SerialName("damage")
    val damage: List<Damage>,
    @SerialName("desc")
    val desc: String,
    @SerialName("name")
    val name: String
)

@Serializable
data class LegendaryAction(
    @SerialName("attack_bonus")
    val attackBonus: Int? = null,
    @SerialName("desc")
    val desc: String,
    @SerialName("name")
    val name: String
)

@Serializable
data class Senses(
    @SerialName("darkvision")
    val darkvision: String? = null,
    @SerialName("passive_perception")
    val passivePerception: Int? = null
)

@Serializable
data class SpecialAbility(
    @SerialName("desc")
    val desc: String,
    @SerialName("name")
    val name: String,
    @SerialName("spellcasting")
    val spellCasting: SpellCasting? = null,
    @SerialName("dc")
    val dc: DifficultyClass? = null
)

@Serializable
data class Speed(
    @SerialName("burrow")
    val burrow: String? = null,
    @SerialName("climb")
    val climb: String? = null,
    @SerialName("fly")
    val fly: String? = null,
    @SerialName("hover")
    val hover: Boolean? = null,
    @SerialName("walk")
    val walk: String? = null,
    @SerialName("swim")
    val swim: String? = null
)

@Serializable
data class Damage(
    @SerialName("damage_dice")
    val damageDice: String? = null,
    @SerialName("damage_type")
    val damageType: APIReference? = null
)

@Serializable
data class SpellCasting(
    @SerialName("ability")
    val ability: APIReference,
    @SerialName("components_required")
    val componentsRequired: List<String>,
    @SerialName("dc")
    val dc: Int? = null,
    @SerialName("level")
    val level: Int? = null,
    @SerialName("modifier")
    val modifier: Int? = null,
    @SerialName("school")
    val school: String? = null,
    @SerialName("slots")
    val slots: Slots? = null,
    @SerialName("spells")
    val spells: List<Spell>
)

@Serializable
data class Slots(
    @SerialName("1")
    val x1: Int
)

@Serializable
data class Spell(
    @SerialName("level")
    val level: Int,
    @SerialName("name")
    val name: String,
    @SerialName("url")
    val url: String
)

@Serializable
data class DifficultyClass(
    @SerialName("dc_type")
    val dcType: DcType,
    @SerialName("dc_value")
    val dcValue: Int,
    @SerialName("success_type")
    val successType: String
)

@Serializable
data class DcType(
    @SerialName("index")
    val index: String,
    @SerialName("name")
    val name: String,
    @SerialName("url")
    val url: String
)

