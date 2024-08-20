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

package br.alexandregpereira.hunter.data.spell.di

import br.alexandregpereira.hunter.data.spell.DefaultSpellLocalRepository
import br.alexandregpereira.hunter.data.spell.DefaultSpellRemoteRepository
import br.alexandregpereira.hunter.data.spell.DefaultSpellRepository
import br.alexandregpereira.hunter.data.spell.SpellSettingsRepositoryImpl
import br.alexandregpereira.hunter.data.spell.local.DefaultSpellLocalDataSource
import br.alexandregpereira.hunter.data.spell.local.SpellLocalDataSource
import br.alexandregpereira.hunter.data.spell.remote.DefaultSpellRemoteDataSource
import br.alexandregpereira.hunter.data.spell.remote.SpellRemoteDataSource
import br.alexandregpereira.hunter.domain.spell.SpellLocalRepository
import br.alexandregpereira.hunter.domain.spell.SpellRemoteRepository
import br.alexandregpereira.hunter.domain.spell.SpellRepository
import br.alexandregpereira.hunter.domain.spell.SpellSettingsRepository
import org.koin.dsl.module

val spellDataModule = module {
    factory<SpellLocalDataSource> { DefaultSpellLocalDataSource(get()) }
    factory<SpellRepository> { DefaultSpellRepository(get(), get()) }
    factory<SpellLocalRepository> { DefaultSpellLocalRepository(get()) }
    factory<SpellRemoteRepository> { DefaultSpellRemoteRepository(get()) }
    factory<SpellRemoteDataSource> { DefaultSpellRemoteDataSource(get(), get()) }
    factory<SpellSettingsRepository> { SpellSettingsRepositoryImpl(get()) }
}
