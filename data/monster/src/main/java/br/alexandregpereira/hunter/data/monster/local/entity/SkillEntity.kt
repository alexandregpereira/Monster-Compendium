package br.alexandregpereira.hunter.data.monster.local.entity

import androidx.room.Embedded
import androidx.room.Entity

@Entity(primaryKeys = ["index", "monsterIndex"])
data class SkillEntity(
    @Embedded val value: ProficiencyEntity
)
