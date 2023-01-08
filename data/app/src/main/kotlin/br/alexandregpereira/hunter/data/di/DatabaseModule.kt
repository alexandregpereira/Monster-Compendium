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

package br.alexandregpereira.hunter.data.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import br.alexandregpereira.hunter.data.AppDatabase
import br.alexandregpereira.hunter.data.monster.folder.local.dao.MonsterFolderDao
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
import br.alexandregpereira.hunter.data.monster.lore.local.dao.MonsterLoreDao
import br.alexandregpereira.hunter.data.monster.spell.local.dao.SpellUsageDao
import br.alexandregpereira.hunter.data.monster.spell.local.dao.SpellcastingDao
import br.alexandregpereira.hunter.data.spell.local.dao.SpellDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class DatabaseModule {

    @Singleton
    @Provides
    internal fun provideAppDataBase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "hunter-database")
            .build()
    }

    @Provides
    internal fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences("preferences", Context.MODE_PRIVATE)
    }

    @Provides
    internal fun provideAbilityScoreDao(appDatabase: AppDatabase): AbilityScoreDao {
        return appDatabase.abilityScoreDao()
    }

    @Provides
    internal fun provideActionDao(appDatabase: AppDatabase): ActionDao {
        return appDatabase.actionDao()
    }

    @Provides
    internal fun provideConditionDao(appDatabase: AppDatabase): ConditionDao {
        return appDatabase.conditionDao()
    }

    @Provides
    internal fun provideDamageDao(appDatabase: AppDatabase): DamageDao {
        return appDatabase.damageDao()
    }

    @Provides
    internal fun provideDamageDiceDao(appDatabase: AppDatabase): DamageDiceDao {
        return appDatabase.damageDiceDao()
    }

    @Provides
    internal fun provideLegendaryActionDao(appDatabase: AppDatabase): LegendaryActionDao {
        return appDatabase.legendaryActionDao()
    }

    @Provides
    internal fun provideMonsterDao(appDatabase: AppDatabase): MonsterDao {
        return appDatabase.monsterDao()
    }

    @Provides
    internal fun provideMonsterFolderDao(appDatabase: AppDatabase): MonsterFolderDao {
        return appDatabase.monsterFolderDao()
    }

    @Provides
    internal fun provideMonsterLoreDao(appDatabase: AppDatabase): MonsterLoreDao {
        return appDatabase.monsterLoreDao()
    }

    @Provides
    internal fun provideSavingThrowDao(appDatabase: AppDatabase): SavingThrowDao {
        return appDatabase.savingThrowDao()
    }

    @Provides
    internal fun provideSkillDao(appDatabase: AppDatabase): SkillDao {
        return appDatabase.skillDao()
    }

    @Provides
    internal fun provideSpecialAbilityDao(appDatabase: AppDatabase): SpecialAbilityDao {
        return appDatabase.specialAbilityDao()
    }

    @Provides
    internal fun provideSpeedDao(appDatabase: AppDatabase): SpeedDao {
        return appDatabase.speedDao()
    }

    @Provides
    internal fun provideSpeedValueDao(appDatabase: AppDatabase): SpeedValueDao {
        return appDatabase.speedValueDao()
    }

    @Provides
    internal fun provideReactionDao(appDatabase: AppDatabase): ReactionDao {
        return appDatabase.reactionDao()
    }

    @Provides
    internal fun provideSpellDao(appDatabase: AppDatabase): SpellDao {
        return appDatabase.spellDao()
    }

    @Provides
    internal fun provideSpellcastingDao(appDatabase: AppDatabase): SpellcastingDao {
        return appDatabase.spellcastingDao()
    }

    @Provides
    internal fun provideSpellUsageDao(appDatabase: AppDatabase): SpellUsageDao {
        return appDatabase.spellUsageDao()
    }
}
