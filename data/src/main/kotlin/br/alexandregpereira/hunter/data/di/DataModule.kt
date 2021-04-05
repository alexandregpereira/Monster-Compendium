/*
 * Copyright (c) 2021 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("EXPERIMENTAL_API_USAGE")

package br.alexandregpereira.hunter.data.di

import androidx.room.Room
import br.alexandregpereira.hunter.data.AppDatabase
import br.alexandregpereira.hunter.data.MonsterRepositoryImpl
import br.alexandregpereira.hunter.data.local.MonsterLocalDataSource
import br.alexandregpereira.hunter.data.local.MonsterLocalDataSourceImpl
import br.alexandregpereira.hunter.data.remote.MonsterApi
import br.alexandregpereira.hunter.data.remote.MonsterRemoteDataSource
import br.alexandregpereira.hunter.data.remote.MonsterRemoteDataSourceImpl
import br.alexandregpereira.hunter.domain.MonsterRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.koin.dsl.module
import retrofit2.Retrofit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext

val dataModule = module {
    single {
        val json = Json {
            ignoreUnknownKeys = true
        }
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
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

    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java, "hunter-database"
        ).build()
    }

    single { get<AppDatabase>().monsterDao() }

    single<MonsterLocalDataSource> { MonsterLocalDataSourceImpl(get()) }
}
