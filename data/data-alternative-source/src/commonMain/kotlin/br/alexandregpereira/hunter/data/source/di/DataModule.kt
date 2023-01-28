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
import br.alexandregpereira.hunter.data.source.remote.AlternativeSourceRepositoryImpl
import br.alexandregpereira.hunter.data.source.remote.DefaultAlternativeSourceRemoteDataSource
import br.alexandregpereira.hunter.domain.source.AlternativeSourceRepository
import org.koin.core.module.Module
import org.koin.core.scope.Scope
import org.koin.dsl.module

val alternativeSourceDataModule = module {
    factory { createRemoteDataSource() ?: DefaultAlternativeSourceRemoteDataSource() }
    factory<AlternativeSourceRepository> { AlternativeSourceRepositoryImpl(get()) }
}.apply { includes(getAdditionalModule()) }

internal expect fun getAdditionalModule(): Module

internal expect fun Scope.createRemoteDataSource(): AlternativeSourceRemoteDataSource?
