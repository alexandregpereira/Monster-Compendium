package br.alexandregpereira.hunter.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity

@Entity(primaryKeys = ["index", "monsterIndex"])
internal data class SkillEntity(
    @Embedded val value: ProficiencyEntity
)
