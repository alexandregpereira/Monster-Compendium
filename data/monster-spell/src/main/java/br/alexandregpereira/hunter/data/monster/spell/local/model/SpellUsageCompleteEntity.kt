/*
 * Hunter - DnD 5th edition monster compendium application
 * Copyright (C) 2022. Alexandre Gomes Pereira.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.data.monster.spell.local.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class SpellUsageCompleteEntity(
    @Embedded val spellUsage: SpellUsageEntity,
    @Relation(
        parentColumn = "spellUsageId",
        entityColumn = "spellIndex",
        associateBy = Junction(SpellUsageSpellCrossRefEntity::class),
    )
    val spells: List<SpellPreviewEntity>
)
