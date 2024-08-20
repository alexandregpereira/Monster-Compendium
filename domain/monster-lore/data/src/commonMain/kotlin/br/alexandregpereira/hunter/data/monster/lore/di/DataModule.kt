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

package br.alexandregpereira.hunter.data.monster.lore.di

import br.alexandregpereira.hunter.data.monster.lore.DefaultMonsterLoreLocalRepository
import br.alexandregpereira.hunter.data.monster.lore.DefaultMonsterLoreRemoteRepository
import br.alexandregpereira.hunter.data.monster.lore.DefaultMonsterLoreRepository
import br.alexandregpereira.hunter.data.monster.lore.MonsterLoreSettingsRepositoryImpl
import br.alexandregpereira.hunter.data.monster.lore.MonsterLoreSourceRepositoryImpl
import br.alexandregpereira.hunter.data.monster.lore.local.MonsterLoreLocalDataSource
import br.alexandregpereira.hunter.data.monster.lore.remote.DefaultMonsterLoreRemoteDataSource
import br.alexandregpereira.hunter.data.monster.lore.remote.MonsterLoreRemoteDataSource
import br.alexandregpereira.hunter.domain.monster.lore.MonsterLoreLocalRepository
import br.alexandregpereira.hunter.domain.monster.lore.MonsterLoreRemoteRepository
import br.alexandregpereira.hunter.domain.monster.lore.MonsterLoreRepository
import br.alexandregpereira.hunter.domain.monster.lore.MonsterLoreSettingsRepository
import br.alexandregpereira.hunter.domain.monster.lore.MonsterLoreSourceRepository
import org.koin.core.module.Module
import org.koin.core.scope.Scope
import org.koin.dsl.module

val monsterLoreDataModule = module {
    factory<MonsterLoreRepository> { DefaultMonsterLoreRepository(get(), get()) }
    single { MonsterLoreLocalDataSource(get()) }
    factory { createRemoteDataSource() ?: DefaultMonsterLoreRemoteDataSource(get(), get()) }
    factory { createLocalRepository() ?: DefaultMonsterLoreLocalRepository(get()) }
    factory<MonsterLoreRemoteRepository> { DefaultMonsterLoreRemoteRepository(get()) }
    factory<MonsterLoreSettingsRepository> { MonsterLoreSettingsRepositoryImpl(get()) }
    factory<MonsterLoreSourceRepository> { MonsterLoreSourceRepositoryImpl(get()) }
}.apply { includes(getAdditionalModule()) }

internal expect fun getAdditionalModule(): Module

internal expect fun Scope.createRemoteDataSource(): MonsterLoreRemoteDataSource?

internal expect fun Scope.createLocalRepository(): MonsterLoreLocalRepository?
