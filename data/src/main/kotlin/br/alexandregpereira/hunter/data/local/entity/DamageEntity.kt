package br.alexandregpereira.hunter.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity

internal sealed class DamageEntity {
    abstract val value: ValueEntity
}

@Entity(primaryKeys = ["index", "monsterIndex"])
internal data class DamageVulnerabilityEntity(
    @Embedded override val value: ValueEntity,
) : DamageEntity()

@Entity(primaryKeys = ["index", "monsterIndex"])
internal data class DamageResistanceEntity(
    @Embedded override val value: ValueEntity,
) : DamageEntity()

@Entity(primaryKeys = ["index", "monsterIndex"])
internal data class DamageImmunityEntity(
    @Embedded override val value: ValueEntity,
) : DamageEntity()
