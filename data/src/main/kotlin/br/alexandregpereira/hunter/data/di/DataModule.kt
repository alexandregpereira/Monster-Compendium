/*
 * Hunter - DnD 5th edition monster compendium application
 * Copyright (C) 2021 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

@file:Suppress("EXPERIMENTAL_API_USAGE")

package br.alexandregpereira.hunter.data.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import br.alexandregpereira.hunter.data.AppDatabase
import br.alexandregpereira.hunter.data.MonsterRepositoryImpl
import br.alexandregpereira.hunter.data.local.MonsterLocalDataSource
import br.alexandregpereira.hunter.data.local.MonsterLocalDataSourceImpl
import br.alexandregpereira.hunter.data.local.dao.AbilityScoreDao
import br.alexandregpereira.hunter.data.local.dao.ActionDao
import br.alexandregpereira.hunter.data.local.dao.ConditionDao
import br.alexandregpereira.hunter.data.local.dao.DamageDao
import br.alexandregpereira.hunter.data.local.dao.DamageDiceDao
import br.alexandregpereira.hunter.data.local.dao.MonsterDao
import br.alexandregpereira.hunter.data.local.dao.ReactionDao
import br.alexandregpereira.hunter.data.local.dao.SavingThrowDao
import br.alexandregpereira.hunter.data.local.dao.SkillDao
import br.alexandregpereira.hunter.data.local.dao.SpecialAbilityDao
import br.alexandregpereira.hunter.data.local.dao.SpeedDao
import br.alexandregpereira.hunter.data.local.dao.SpeedValueDao
import br.alexandregpereira.hunter.data.preferences.PreferencesDataSource
import br.alexandregpereira.hunter.data.preferences.PreferencesDataSourceImpl
import br.alexandregpereira.hunter.data.preferences.PreferencesRepository
import br.alexandregpereira.hunter.data.remote.MonsterApi
import br.alexandregpereira.hunter.data.remote.MonsterRemoteDataSource
import br.alexandregpereira.hunter.data.remote.MonsterRemoteDataSourceImpl
import br.alexandregpereira.hunter.data.source.AlternativeSourceApi
import br.alexandregpereira.hunter.data.source.AlternativeSourceRemoteDataSource
import br.alexandregpereira.hunter.data.source.AlternativeSourceRemoteDataSourceImpl
import br.alexandregpereira.hunter.data.source.AlternativeSourceRepositoryImpl
import br.alexandregpereira.hunter.domain.repository.AlternativeSourceRepository
import br.alexandregpereira.hunter.domain.repository.CompendiumRepository
import br.alexandregpereira.hunter.domain.repository.MeasurementUnitRepository
import br.alexandregpereira.hunter.domain.repository.MonsterRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

internal class RetrofitWrapper {
    val retrofit: Retrofit

    init {
        val json = Json {
            ignoreUnknownKeys = true
        }
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC)
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val baseUrl = "https://raw.githubusercontent.com/alexandregpereira/hunter-api/main/json/"
        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }
}

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(): RetrofitWrapper {
        return RetrofitWrapper()
    }

    @Provides
    fun provideMonsterApi(retrofitWrapper: RetrofitWrapper): MonsterApi {
        return retrofitWrapper.retrofit.create(MonsterApi::class.java)
    }

    @Provides
    fun provideAlternativeSourceApi(retrofitWrapper: RetrofitWrapper): AlternativeSourceApi {
        return retrofitWrapper.retrofit.create(AlternativeSourceApi::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    internal fun provideAppDataBase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "hunter-database")
            .fallbackToDestructiveMigration()
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
    internal fun provideMonsterDao(appDatabase: AppDatabase): MonsterDao {
        return appDatabase.monsterDao()
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
}

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataModule {

    @Binds
    abstract fun bindMonsterRemoteDataSource(
        monsterRemoteDataSourceImpl: MonsterRemoteDataSourceImpl
    ): MonsterRemoteDataSource

    @Binds
    abstract fun bindMonsterLocalDataSource(
        monsterLocalDataSourceImpl: MonsterLocalDataSourceImpl
    ): MonsterLocalDataSource

    @Binds
    abstract fun bindPreferencesDataSource(
        preferencesDataSourceImpl: PreferencesDataSourceImpl
    ): PreferencesDataSource

    @Binds
    abstract fun bindAlternativeSourceRemoteDataSource(
        alternativeSourceRemoteDataSourceImpl: AlternativeSourceRemoteDataSourceImpl
    ): AlternativeSourceRemoteDataSource
}

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindMonsterRepository(
        monsterRepositoryImpl: MonsterRepositoryImpl
    ): MonsterRepository

    @Singleton
    @Binds
    abstract fun bindCompendiumRepository(
        preferencesRepository: PreferencesRepository
    ): CompendiumRepository

    @Singleton
    @Binds
    abstract fun bindMeasurementUnitRepository(
        preferencesRepository: PreferencesRepository
    ): MeasurementUnitRepository

    @Singleton
    @Binds
    abstract fun bindAlternativeSourceRepository(
        alternativeSourceRepositoryImpl: AlternativeSourceRepositoryImpl
    ): AlternativeSourceRepository
}
