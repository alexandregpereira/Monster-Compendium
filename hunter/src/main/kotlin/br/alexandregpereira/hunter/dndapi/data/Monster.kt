package br.alexandregpereira.hunter.dndapi.data

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
    val reactions: List<Reaction> = emptyList(),
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
    val name: String,
    val options: Option? = null,
    val dc: DifficultyClass? = null,
    val usage: Usage? = null,
    @SerialName("attack_options")
    val attackOption: AttackOption? = null,
    val attacks: List<Attack> = emptyList(),
    @SerialName("damage_dice")
    val damageDice: String? = null
)

@Serializable
data class LegendaryAction(
    @SerialName("attack_bonus")
    val attackBonus: Int? = null,
    @SerialName("desc")
    val desc: String,
    @SerialName("name")
    val name: String,
    val dc: DifficultyClass? = null,
    val damage: List<Damage> = emptyList()
)

@Serializable
data class Senses(
    @SerialName("blindsight")
    val blindsight: String? = null,
    @SerialName("darkvision")
    val darkvision: String? = null,
    val truesight: String? = null,
    val tremorsense: String? = null,
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
    val dc: DifficultyClass? = null,
    val usage: Usage? = null,
    @SerialName("damage")
    val damages: List<Damage> = emptyList(),
    @SerialName("attack_bonus")
    val attackBonus: Int? = null
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
    val hover: Boolean = false,
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
    val damageType: APIReference? = null,
    val dc: DifficultyClass? = null
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
    val slots: Map<String, Int> = mapOf(),
    @SerialName("spells")
    val spells: List<Spell>
)

@Serializable
data class Spell(
    @SerialName("level")
    val level: Int,
    @SerialName("name")
    val name: String,
    @SerialName("url")
    val url: String,
    val usage: Usage? = null,
    val notes: String? = null
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

@Serializable
data class Option(
    val choose: Int,
    val from: List<List<From>>
)

@Serializable
data class From(
    val name: String,
    val type: String,
    val notes: String? = null
)

@Serializable
data class Usage(
    val type: String,
    val dice: String? = null,
    @SerialName("min_value")
    val minValue: Int = 0,
    val times: Int = 0,
    @SerialName("rest_types")
    val restTypes: List<String> = emptyList()
)

@Serializable
data class AttackOption(
    val choose: Int,
    val type: String,
    val from: List<Attack>
)

@Serializable
data class Attack(
    val name: String,
    val dc: DifficultyClass,
    val damage: List<Damage> = emptyList()
)

@Serializable
data class Reaction(
    val name: String,
    val desc: String,
    val dc: DifficultyClass? = null
)
