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

package br.alexandregpereira.hunter.data.spell.di

import br.alexandregpereira.hunter.data.spell.DefaultSettingsSpellDataRepository
import br.alexandregpereira.hunter.data.spell.DefaultSpellLocalRepository
import br.alexandregpereira.hunter.data.spell.DefaultSpellRemoteRepository
import br.alexandregpereira.hunter.data.spell.DefaultSpellRepository
import br.alexandregpereira.hunter.data.spell.SpellSettingsRepositoryImpl
import br.alexandregpereira.hunter.data.spell.local.DefaultSpellLocalDataSource
import br.alexandregpereira.hunter.data.spell.local.SpellLocalDataSource
import br.alexandregpereira.hunter.data.spell.remote.DefaultSpellRemoteDataSource
import br.alexandregpereira.hunter.data.spell.remote.SpellRemoteDataSource
import br.alexandregpereira.hunter.domain.settings.SettingsSpellDataRepository
import br.alexandregpereira.hunter.domain.spell.SpellLocalRepository
import br.alexandregpereira.hunter.domain.spell.SpellRemoteRepository
import br.alexandregpereira.hunter.domain.spell.SpellRepository
import br.alexandregpereira.hunter.domain.spell.SpellSettingsRepository
import org.koin.core.module.Module
import org.koin.core.scope.Scope
import org.koin.dsl.module

val spellDataModule = module {
    factory<SpellLocalDataSource> { DefaultSpellLocalDataSource(get()) }
    factory<SpellRepository> { DefaultSpellRepository(get(), get()) }
    factory { createLocalRepository() ?: DefaultSpellLocalRepository(get()) }
    factory<SpellRemoteRepository> { DefaultSpellRemoteRepository(get()) }
    factory<SpellRemoteDataSource> { DefaultSpellRemoteDataSource(get(), get()) }
    factory { createSettingsRepository() ?: DefaultSettingsSpellDataRepository(get()) }
    factory<SpellSettingsRepository> { SpellSettingsRepositoryImpl(get()) }
}.apply { includes(getAdditionalModule()) }

internal expect fun getAdditionalModule(): Module

internal expect fun Scope.createLocalRepository(): SpellLocalRepository?

internal expect fun Scope.createSettingsRepository(): SettingsSpellDataRepository?
