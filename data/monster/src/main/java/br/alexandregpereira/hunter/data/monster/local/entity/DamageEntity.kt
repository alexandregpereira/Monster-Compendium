package br.alexandregpereira.hunter.data.monster.local.entity

import androidx.room.Embedded
import androidx.room.Entity

sealed class DamageEntity {
    abstract val value: ValueEntity
}

@Entity(primaryKeys = ["index", "monsterIndex"])
data class DamageVulnerabilityEntity(
    @Embedded override val value: ValueEntity,
) : DamageEntity()

@Entity(primaryKeys = ["index", "monsterIndex"])
data class DamageResistanceEntity(
    @Embedded override val value: ValueEntity,
) : DamageEntity()

@Entity(primaryKeys = ["index", "monsterIndex"])
data class DamageImmunityEntity(
    @Embedded override val value: ValueEntity,
) : DamageEntity()
