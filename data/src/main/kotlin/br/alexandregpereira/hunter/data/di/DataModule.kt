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
import br.alexandregpereira.hunter.data.local.dao.MonsterDao
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
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

internal class RetrofitWrapper {
    val retrofit: Retrofit by lazy {
        val json = Json {
            ignoreUnknownKeys = true
        }
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC)
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        Retrofit.Builder()
            .baseUrl("https://raw.githubusercontent.com/alexandregpereira/hunter/main/json/")
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

    @Singleton
    @Provides
    fun provideMonsterApi(retrofitWrapper: RetrofitWrapper): MonsterApi {
        return retrofitWrapper.retrofit.create(MonsterApi::class.java)
    }

    @Singleton
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
    internal fun provideMonsterDao(appDatabase: AppDatabase): MonsterDao {
        return appDatabase.monsterDao()
    }
}

@Module
@InstallIn(ActivityComponent::class)
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
@InstallIn(ActivityComponent::class)
internal abstract class RepositoryModule {

    @Binds
    abstract fun bindMonsterRepository(
        monsterRepositoryImpl: MonsterRepositoryImpl
    ): MonsterRepository

    @Binds
    abstract fun bindCompendiumRepository(
        preferencesRepository: PreferencesRepository
    ): CompendiumRepository

    @Binds
    abstract fun bindMeasurementUnitRepository(
        preferencesRepository: PreferencesRepository
    ): MeasurementUnitRepository

    @Binds
    abstract fun bindAlternativeSourceRepository(
        alternativeSourceRepositoryImpl: AlternativeSourceRepositoryImpl
    ): AlternativeSourceRepository
}
