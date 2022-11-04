/*
 * Copyright 2022 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.alexandregpereira.hunter.data

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import br.alexandregpereira.hunter.data.monster.folder.local.dao.MonsterFolderDao
import br.alexandregpereira.hunter.data.monster.folder.local.entity.MonsterFolderEntity
import br.alexandregpereira.hunter.data.monster.local.dao.AbilityScoreDao
import br.alexandregpereira.hunter.data.monster.local.dao.ActionDao
import br.alexandregpereira.hunter.data.monster.local.dao.ConditionDao
import br.alexandregpereira.hunter.data.monster.local.dao.DamageDao
import br.alexandregpereira.hunter.data.monster.local.dao.DamageDiceDao
import br.alexandregpereira.hunter.data.monster.local.dao.LegendaryActionDao
import br.alexandregpereira.hunter.data.monster.local.dao.MonsterDao
import br.alexandregpereira.hunter.data.monster.local.dao.ReactionDao
import br.alexandregpereira.hunter.data.monster.local.dao.SavingThrowDao
import br.alexandregpereira.hunter.data.monster.local.dao.SkillDao
import br.alexandregpereira.hunter.data.monster.local.dao.SpecialAbilityDao
import br.alexandregpereira.hunter.data.monster.local.dao.SpeedDao
import br.alexandregpereira.hunter.data.monster.local.dao.SpeedValueDao
import br.alexandregpereira.hunter.data.monster.local.entity.AbilityScoreEntity
import br.alexandregpereira.hunter.data.monster.local.entity.ActionEntity
import br.alexandregpereira.hunter.data.monster.local.entity.ConditionEntity
import br.alexandregpereira.hunter.data.monster.local.entity.DamageDiceEntity
import br.alexandregpereira.hunter.data.monster.local.entity.DamageImmunityEntity
import br.alexandregpereira.hunter.data.monster.local.entity.DamageResistanceEntity
import br.alexandregpereira.hunter.data.monster.local.entity.DamageVulnerabilityEntity
import br.alexandregpereira.hunter.data.monster.local.entity.LegendaryActionEntity
import br.alexandregpereira.hunter.data.monster.local.entity.MonsterEntity
import br.alexandregpereira.hunter.data.monster.local.entity.ReactionEntity
import br.alexandregpereira.hunter.data.monster.local.entity.SavingThrowEntity
import br.alexandregpereira.hunter.data.monster.local.entity.SkillEntity
import br.alexandregpereira.hunter.data.monster.local.entity.SpecialAbilityEntity
import br.alexandregpereira.hunter.data.monster.local.entity.SpeedEntity
import br.alexandregpereira.hunter.data.monster.local.entity.SpeedValueEntity
import br.alexandregpereira.hunter.data.monster.spell.local.dao.SpellUsageDao
import br.alexandregpereira.hunter.data.monster.spell.local.dao.SpellcastingDao
import br.alexandregpereira.hunter.data.monster.spell.local.model.SpellPreviewEntity
import br.alexandregpereira.hunter.data.monster.spell.local.model.SpellUsageEntity
import br.alexandregpereira.hunter.data.monster.spell.local.model.SpellUsageSpellCrossRefEntity
import br.alexandregpereira.hunter.data.monster.spell.local.model.SpellcastingEntity
import br.alexandregpereira.hunter.data.monster.spell.local.model.SpellcastingSpellUsageCrossRefEntity
import br.alexandregpereira.hunter.data.spell.local.dao.SpellDao
import br.alexandregpereira.hunter.data.spell.local.model.SpellEntity

@Database(
    entities = [
        AbilityScoreEntity::class,
        ActionEntity::class,
        ConditionEntity::class,
        DamageVulnerabilityEntity::class,
        DamageResistanceEntity::class,
        DamageImmunityEntity::class,
        DamageDiceEntity::class,
        LegendaryActionEntity::class,
        MonsterEntity::class,
        MonsterFolderEntity::class,
        ReactionEntity::class,
        SavingThrowEntity::class,
        SkillEntity::class,
        SpecialAbilityEntity::class,
        SpeedEntity::class,
        SpeedValueEntity::class,
        SpellEntity::class,
        SpellcastingEntity::class,
        SpellcastingSpellUsageCrossRefEntity::class,
        SpellUsageEntity::class,
        SpellUsageSpellCrossRefEntity::class,
        SpellPreviewEntity::class,
    ],
    version = 19,
    autoMigrations = [ AutoMigration(from = 17, to = 18), AutoMigration(from = 18, to = 19) ]
)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun abilityScoreDao(): AbilityScoreDao
    abstract fun actionDao(): ActionDao
    abstract fun conditionDao(): ConditionDao
    abstract fun damageDao(): DamageDao
    abstract fun damageDiceDao(): DamageDiceDao
    abstract fun legendaryActionDao(): LegendaryActionDao
    abstract fun monsterDao(): MonsterDao
    abstract fun monsterFolderDao(): MonsterFolderDao
    abstract fun reactionDao(): ReactionDao
    abstract fun savingThrowDao(): SavingThrowDao
    abstract fun skillDao(): SkillDao
    abstract fun specialAbilityDao(): SpecialAbilityDao
    abstract fun speedDao(): SpeedDao
    abstract fun speedValueDao(): SpeedValueDao
    abstract fun spellcastingDao(): SpellcastingDao
    abstract fun spellDao(): SpellDao
    abstract fun spellUsageDao(): SpellUsageDao
}
