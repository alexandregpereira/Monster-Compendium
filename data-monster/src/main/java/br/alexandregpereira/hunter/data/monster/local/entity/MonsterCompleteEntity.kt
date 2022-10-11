package br.alexandregpereira.hunter.data.monster.local.entity

import androidx.room.Embedded
import androidx.room.Relation
import br.alexandregpereira.hunter.data.monster.spell.local.model.SpellcastingCompleteEntity
import br.alexandregpereira.hunter.data.monster.spell.local.model.SpellcastingEntity

data class MonsterCompleteEntity(
    @Embedded val monster: MonsterEntity,
    @Relation(
        entity = SpeedEntity::class,
        parentColumn = "index",
        entityColumn = "monsterIndex",
    )
    val speed: SpeedWithValuesEntity?,
    @Relation(
        parentColumn = "index",
        entityColumn = "monsterIndex",
    )
    val abilityScores: List<AbilityScoreEntity>,
    @Relation(
        parentColumn = "index",
        entityColumn = "monsterIndex",
    )
    val savingThrows: List<SavingThrowEntity>,
    @Relation(
        parentColumn = "index",
        entityColumn = "monsterIndex",
    )
    val skills: List<SkillEntity>,
    @Relation(
        parentColumn = "index",
        entityColumn = "monsterIndex",
    )
    val damageVulnerabilities: List<DamageVulnerabilityEntity>,
    @Relation(
        parentColumn = "index",
        entityColumn = "monsterIndex",
    )
    val damageResistances: List<DamageResistanceEntity>,
    @Relation(
        parentColumn = "index",
        entityColumn = "monsterIndex",
    )
    val damageImmunities: List<DamageImmunityEntity>,
    @Relation(
        parentColumn = "index",
        entityColumn = "monsterIndex",
    )
    val conditionImmunities: List<ConditionEntity>,
    @Relation(
        parentColumn = "index",
        entityColumn = "monsterIndex",
    )
    val specialAbilities: List<SpecialAbilityEntity>,
    @Relation(
        entity = ActionEntity::class,
        parentColumn = "index",
        entityColumn = "monsterIndex",
    )
    val actions: List<ActionWithDamageDicesEntity>,
    @Relation(
        parentColumn = "index",
        entityColumn = "monsterIndex",
    )
    val reactions: List<ReactionEntity>,
    @Relation(
        entity = SpellcastingEntity::class,
        parentColumn = "index",
        entityColumn = "monsterIndex",
    )
    val spellcastings: List<SpellcastingCompleteEntity>,
)
