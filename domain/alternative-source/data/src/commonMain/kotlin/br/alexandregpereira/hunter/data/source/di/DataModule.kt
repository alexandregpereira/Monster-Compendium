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

package br.alexandregpereira.hunter.data.source.di

import br.alexandregpereira.hunter.data.source.remote.AlternativeSourceRemoteDataSource
import br.alexandregpereira.hunter.data.source.remote.AlternativeSourceRemoteRepositoryImpl
import br.alexandregpereira.hunter.data.source.AlternativeSourceSettingsRepositoryImpl
import br.alexandregpereira.hunter.data.source.local.AlternativeSourceLocalDataSource
import br.alexandregpereira.hunter.data.source.local.AlternativeSourceLocalRepositoryImpl
import br.alexandregpereira.hunter.data.source.remote.DefaultAlternativeSourceRemoteDataSource
import br.alexandregpereira.hunter.domain.source.AlternativeSourceLocalRepository
import br.alexandregpereira.hunter.domain.source.AlternativeSourceRemoteRepository
import br.alexandregpereira.hunter.domain.source.AlternativeSourceSettingsRepository
import org.koin.dsl.module

val alternativeSourceDataModule = module {
    factory {
        AlternativeSourceLocalDataSource(get())
    }
    factory<AlternativeSourceRemoteDataSource> {
        DefaultAlternativeSourceRemoteDataSource(get(), get())
    }
    factory<AlternativeSourceLocalRepository> { AlternativeSourceLocalRepositoryImpl(get()) }
    factory<AlternativeSourceRemoteRepository> { AlternativeSourceRemoteRepositoryImpl(get()) }
    factory<AlternativeSourceSettingsRepository> { AlternativeSourceSettingsRepositoryImpl(get()) }
}
