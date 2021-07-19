package br.alexandregpereira.hunter.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity

@Entity(primaryKeys = ["index", "monsterIndex"])
internal data class SavingThrowEntity(
    @Embedded val value: ProficiencyEntity
)
