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
import androidx.room.Room
import br.alexandregpereira.hunter.data.AppDatabase
import br.alexandregpereira.hunter.data.MonsterRepositoryImpl
import br.alexandregpereira.hunter.data.local.MonsterLocalDataSource
import br.alexandregpereira.hunter.data.local.MonsterLocalDataSourceImpl
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
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit

val dataModule = module {
    single {
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

    single<MonsterRemoteDataSource> {
        MonsterRemoteDataSourceImpl(get())
    }

    single { get<Retrofit>().create(MonsterApi::class.java) }

    single<MonsterRepository> {
        MonsterRepositoryImpl(get(), get())
    }

    single<CompendiumRepository> {
        PreferencesRepository(get())
    }

    single<MeasurementUnitRepository> {
        PreferencesRepository(get())
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "hunter-database")
            .fallbackToDestructiveMigration()
            .build()
    }

    single { get<AppDatabase>().monsterDao() }

    single<MonsterLocalDataSource> { MonsterLocalDataSourceImpl(get()) }

    single { androidContext().getSharedPreferences("preferences", Context.MODE_PRIVATE) }

    single<PreferencesDataSource> { PreferencesDataSourceImpl(get()) }

    single { get<Retrofit>().create(AlternativeSourceApi::class.java) }

    factory<AlternativeSourceRemoteDataSource> { AlternativeSourceRemoteDataSourceImpl(get()) }

    factory<AlternativeSourceRepository> { AlternativeSourceRepositoryImpl(get()) }
}
