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
