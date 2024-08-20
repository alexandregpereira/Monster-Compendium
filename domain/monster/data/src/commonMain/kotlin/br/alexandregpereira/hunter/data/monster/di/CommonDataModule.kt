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

@file:Suppress("EXPERIMENTAL_API_USAGE")

package br.alexandregpereira.hunter.data.monster.di

import br.alexandregpereira.hunter.data.monster.DefaultMonsterLocalRepository
import br.alexandregpereira.hunter.data.monster.MonsterAlternativeSourceRepositoryImpl
import br.alexandregpereira.hunter.data.monster.MonsterCacheRepositoryImpl
import br.alexandregpereira.hunter.data.monster.MonsterImageRepositoryImpl
import br.alexandregpereira.hunter.data.monster.MonsterRemoteRepositoryImpl
import br.alexandregpereira.hunter.data.monster.MonsterRepositoryImpl
import br.alexandregpereira.hunter.data.monster.MonsterSettingsRepositoryImpl
import br.alexandregpereira.hunter.data.monster.cache.MonsterCacheDataSource
import br.alexandregpereira.hunter.data.monster.local.DefaultMonsterLocalDataSource
import br.alexandregpereira.hunter.data.monster.local.MonsterLocalDataSource
import br.alexandregpereira.hunter.data.monster.preferences.MonsterPreferencesRepository
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
        createMonsterLocalRepository() ?: DefaultMonsterLocalRepository(get(), get())
    }
    factory<MonsterRemoteRepository> { MonsterRemoteRepositoryImpl(get(), get()) }
    factory<MonsterCacheRepository> { MonsterCacheRepositoryImpl(get()) }
    factory<MonsterSettingsRepository> { MonsterSettingsRepositoryImpl(get()) }
    single { MonsterPreferencesRepository(get()) }
    single<CompendiumRepository> { get<MonsterPreferencesRepository>() }
    single<MeasurementUnitRepository> { get<MonsterPreferencesRepository>() }
    factory<MonsterImageRepository> { MonsterImageRepositoryImpl(get(), get()) }
    factory<MonsterAlternativeSourceRepository> { MonsterAlternativeSourceRepositoryImpl(get()) }
    single<MonsterLocalDataSource> {
        DefaultMonsterLocalDataSource(get())
    }
}.apply { includes(getAdditionalModule()) }

internal expect fun getAdditionalModule(): Module

internal expect fun Scope.createMonsterLocalRepository(): MonsterLocalRepository?
