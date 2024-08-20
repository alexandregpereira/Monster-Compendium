/*
 * Copyright (C) 2024 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.data.database.dao

import br.alexandregpereira.hunter.data.monster.local.entity.AbilityScoreEntity
import br.alexandregpereira.hunter.data.monster.local.entity.ActionEntity
import br.alexandregpereira.hunter.data.monster.local.entity.ConditionEntity
import br.alexandregpereira.hunter.data.monster.local.entity.DamageDiceEntity
import br.alexandregpereira.hunter.data.monster.local.entity.DamageImmunityEntity
import br.alexandregpereira.hunter.data.monster.local.entity.DamageResistanceEntity
import br.alexandregpereira.hunter.data.monster.local.entity.DamageVulnerabilityEntity
import br.alexandregpereira.hunter.data.monster.local.entity.MonsterEntity
import br.alexandregpereira.hunter.data.monster.local.entity.ReactionEntity
import br.alexandregpereira.hunter.data.monster.local.entity.SavingThrowEntity
import br.alexandregpereira.hunter.data.monster.local.entity.SkillEntity
import br.alexandregpereira.hunter.data.monster.local.entity.SpecialAbilityEntity
import br.alexandregpereira.hunter.data.monster.local.entity.SpeedEntity
import br.alexandregpereira.hunter.data.monster.local.entity.SpeedValueEntity
import br.alexandregpereira.hunter.data.monster.spell.local.model.SpellPreviewEntity
import br.alexandregpereira.hunter.data.monster.spell.local.model.SpellUsageEntity
import br.alexandregpereira.hunter.data.monster.spell.local.model.SpellUsageSpellCrossRefEntity
import br.alexandregpereira.hunter.data.monster.spell.local.model.SpellcastingEntity
import br.alexandregpereira.hunter.data.monster.spell.local.model.SpellcastingSpellUsageCrossRefEntity
import br.alexandregpereira.hunter.database.AbilityScoreQueries
import br.alexandregpereira.hunter.database.ActionQueries
import br.alexandregpereira.hunter.database.ConditionQueries
import br.alexandregpereira.hunter.database.DamageDiceQueries
import br.alexandregpereira.hunter.database.DamageImmunityQueries
import br.alexandregpereira.hunter.database.DamageResistanceQueries
import br.alexandregpereira.hunter.database.DamageVulnerabilityQueries
import br.alexandregpereira.hunter.database.LegendaryActionQueries
import br.alexandregpereira.hunter.database.MonsterQueries
import br.alexandregpereira.hunter.database.ReactionQueries
import br.alexandregpereira.hunter.database.SavingThrowQueries
import br.alexandregpereira.hunter.database.SkillQueries
import br.alexandregpereira.hunter.database.SpecialAbilityQueries
import br.alexandregpereira.hunter.database.SpeedQueries
import br.alexandregpereira.hunter.database.SpeedValueQueries
import br.alexandregpereira.hunter.database.SpellPreviewQueries
import br.alexandregpereira.hunter.database.SpellUsageQueries
import br.alexandregpereira.hunter.database.SpellUsageSpellCrossRefQueries
import br.alexandregpereira.hunter.database.SpellcastingQueries
import br.alexandregpereira.hunter.database.SpellcastingSpellUsageCrossRefQueries

internal fun MonsterQueries.insert(entities: List<MonsterEntity>) {
    entities.forEach {
        insert(it.toDatabaseEntity())
    }
}

internal fun AbilityScoreQueries.insert(entities: List<AbilityScoreEntity>) {
    entities.forEach {
        insert(it.toDatabaseEntity())
    }
}

internal fun ActionQueries.insert(entities: List<ActionEntity>) = entities.run {
    forEach {
        insert(it.toDatabaseEntity())
    }
}

internal fun ConditionQueries.insert(entities: List<ConditionEntity>) = entities.run {
    forEach {
        insert(it.toDatabaseEntity())
    }
}

internal fun DamageVulnerabilityQueries.insert(entities: List<DamageVulnerabilityEntity>) =
    entities.run {
        forEach {
            insert(it.toDatabaseEntity())
        }
    }

internal fun DamageResistanceQueries.insert(entities: List<DamageResistanceEntity>) {
    entities.forEach {
        insert(it.toDatabaseEntity())
    }
}

internal fun DamageImmunityQueries.insert(entities: List<DamageImmunityEntity>) {
    entities.forEach {
        insert(it.toDatabaseEntity())
    }
}

internal fun DamageDiceQueries.insert(entities: List<DamageDiceEntity>) {
    entities.forEach {
        insert(it.toDatabaseEntity())
    }
}

internal fun LegendaryActionQueries.insert(entities: List<ActionEntity>) {
    entities.forEach {
        insert(it.toLegendaryActionDatabaseEntity())
    }
}

internal fun ReactionQueries.insert(entities: List<ReactionEntity>) {
    entities.forEach {
        insert(it.toDatabaseEntity())
    }
}

internal fun SavingThrowQueries.insert(entities: List<SavingThrowEntity>) {
    entities.forEach {
        insert(it.toDatabaseEntity())
    }
}

internal fun SkillQueries.insert(entities: List<SkillEntity>) {
    entities.forEach {
        insert(it.toDatabaseEntity())
    }
}

internal fun SpecialAbilityQueries.insert(entities: List<SpecialAbilityEntity>) {
    entities.forEach {
        insert(it.toDatabaseEntity())
    }
}

internal fun SpeedQueries.insert(entities: List<SpeedEntity>) {
    entities.forEach {
        insert(it.toDatabaseEntity())
    }
}

internal fun SpeedValueQueries.insert(entities: List<SpeedValueEntity>) {
    entities.forEach {
        insert(it.toDatabaseEntity())
    }
}

internal fun SpellcastingQueries.insert(entities: List<SpellcastingEntity>) {
    entities.forEach {
        insert(it.toDatabaseEntity())
    }
}

internal fun SpellcastingSpellUsageCrossRefQueries.insert(entities: List<SpellcastingSpellUsageCrossRefEntity>) {
    entities.forEach {
        insert(it.toDatabaseEntity())
    }
}

internal fun SpellUsageQueries.insert(entities: List<SpellUsageEntity>) {
    entities.forEach {
        insert(it.toDatabaseEntity())
    }
}

internal fun SpellUsageSpellCrossRefQueries.insert(entities: List<SpellUsageSpellCrossRefEntity>) {
    entities.forEach {
        insert(it.toDatabaseEntity())
    }
}

internal fun SpellPreviewQueries.insert(entities: List<SpellPreviewEntity>) {
    entities.forEach {
        insert(it.toDatabaseEntity())
    }
}
