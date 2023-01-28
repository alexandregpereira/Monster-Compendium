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

package br.alexandregpereira.hunter.data.monster.lore.di

import br.alexandregpereira.hunter.data.monster.lore.DefaultMonsterLoreRepository
import br.alexandregpereira.hunter.data.monster.lore.MonsterLoreSettingsRepositoryImpl
import br.alexandregpereira.hunter.data.monster.lore.MonsterLoreSourceRepositoryImpl
import br.alexandregpereira.hunter.data.monster.lore.remote.DefaultMonsterLoreRemoteDataSource
import br.alexandregpereira.hunter.data.monster.lore.remote.MonsterLoreRemoteDataSource
import br.alexandregpereira.hunter.domain.monster.lore.MonsterLoreRepository
import br.alexandregpereira.hunter.domain.monster.lore.MonsterLoreSettingsRepository
import br.alexandregpereira.hunter.domain.monster.lore.MonsterLoreSourceRepository
import org.koin.core.module.Module
import org.koin.core.scope.Scope
import org.koin.dsl.module

val monsterLoreDataModule = module {
    single { createRemoteDataSource() ?: DefaultMonsterLoreRemoteDataSource() }
    factory { createRepository() ?: DefaultMonsterLoreRepository(get()) }
    factory<MonsterLoreSettingsRepository> { MonsterLoreSettingsRepositoryImpl(get()) }
    factory<MonsterLoreSourceRepository> { MonsterLoreSourceRepositoryImpl(get()) }
}.apply { includes(getAdditionalModule()) }

internal expect fun getAdditionalModule(): Module

internal expect fun Scope.createRemoteDataSource(): MonsterLoreRemoteDataSource?

internal expect fun Scope.createRepository(): MonsterLoreRepository?
