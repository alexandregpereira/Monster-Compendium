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

@file:Suppress("EXPERIMENTAL_API_USAGE")

package br.alexandregpereira.hunter.data.monster.di

import br.alexandregpereira.hunter.data.monster.DefaultMonsterLocalRepository
import br.alexandregpereira.hunter.data.monster.MonsterAlternativeSourceRepositoryImpl
import br.alexandregpereira.hunter.data.monster.MonsterCacheRepositoryImpl
import br.alexandregpereira.hunter.data.monster.MonsterImageRepositoryImpl
import br.alexandregpereira.hunter.data.monster.MonsterRemoteRepositoryImpl
import br.alexandregpereira.hunter.data.monster.MonsterRepositoryImpl
import br.alexandregpereira.hunter.data.monster.MonsterSettingsRepositoryImpl
import br.alexandregpereira.hunter.data.monster.SettingsMonsterDataRepositoryImpl
import br.alexandregpereira.hunter.data.monster.cache.MonsterCacheDataSource
import br.alexandregpereira.hunter.data.monster.local.MonsterLocalDataSource
import br.alexandregpereira.hunter.data.monster.local.DefaultMonsterLocalDataSource
import br.alexandregpereira.hunter.data.monster.preferences.DefaultPreferencesDataSource
import br.alexandregpereira.hunter.data.monster.preferences.PreferencesDataSource
import br.alexandregpereira.hunter.data.monster.preferences.PreferencesRepository
import br.alexandregpereira.hunter.data.monster.remote.DefaultMonsterRemoteDataSource
import br.alexandregpereira.hunter.data.monster.remote.MonsterRemoteDataSource
import br.alexandregpereira.hunter.data.monster.remote.MonsterRemoteDataSourceErrorHandler
import br.alexandregpereira.hunter.domain.repository.CompendiumRepository
import br.alexandregpereira.hunter.domain.repository.MeasurementUnitRepository
import br.alexandregpereira.hunter.domain.repository.MonsterAlternativeSourceRepository
import br.alexandregpereira.hunter.domain.repository.MonsterCacheRepository
import br.alexandregpereira.hunter.domain.repository.MonsterImageRepository
import br.alexandregpereira.hunter.domain.repository.MonsterLocalRepository
import br.alexandregpereira.hunter.domain.repository.MonsterRemoteRepository
import br.alexandregpereira.hunter.domain.repository.MonsterRepository
import br.alexandregpereira.hunter.domain.repository.MonsterSettingsRepository
import br.alexandregpereira.hunter.domain.settings.SettingsMonsterDataRepository
import org.koin.core.module.Module
import org.koin.core.scope.Scope
import org.koin.dsl.module

val monsterDataModule = module {
    single { DefaultMonsterRemoteDataSource(get(), get()) }
    single<MonsterRemoteDataSource> {
        get<DefaultMonsterRemoteDataSource>()
    }
    factory<MonsterRemoteDataSourceErrorHandler> {
        get<DefaultMonsterRemoteDataSource>()
    }
    single { MonsterCacheDataSource() }
    factory<MonsterRepository> { MonsterRepositoryImpl(get(), get()) }
    factory {
        createMonsterLocalRepository() ?: DefaultMonsterLocalRepository(get())
    }
    factory<MonsterRemoteRepository> { MonsterRemoteRepositoryImpl(get(), get()) }
    factory<MonsterCacheRepository> { MonsterCacheRepositoryImpl(get()) }
    factory<MonsterSettingsRepository> { MonsterSettingsRepositoryImpl(get()) }
    factory {
        createPreferencesDataSource() ?: DefaultPreferencesDataSource()
    }
    single { PreferencesRepository(get()) }
    single<CompendiumRepository> { get<PreferencesRepository>() }
    single<MeasurementUnitRepository> { get<PreferencesRepository>() }
    factory<SettingsMonsterDataRepository> { SettingsMonsterDataRepositoryImpl(get(), get()) }
    factory<MonsterImageRepository> { MonsterImageRepositoryImpl(get(), get()) }
    factory<MonsterAlternativeSourceRepository> { MonsterAlternativeSourceRepositoryImpl(get()) }
    single<MonsterLocalDataSource> {
        DefaultMonsterLocalDataSource(get())
    }
}.apply { includes(getAdditionalModule()) }

internal expect fun getAdditionalModule(): Module

internal expect fun Scope.createPreferencesDataSource(): PreferencesDataSource?

internal expect fun Scope.createMonsterLocalRepository(): MonsterLocalRepository?
