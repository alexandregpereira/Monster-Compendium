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
import br.alexandregpereira.hunter.data.source.remote.AlternativeSourceRemoteDataSourceImpl
import br.alexandregpereira.hunter.data.source.remote.AlternativeSourceRepositoryImpl
import br.alexandregpereira.hunter.domain.source.AlternativeSourceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataModule {

    @Binds
    abstract fun bindAlternativeSourceRemoteDataSource(
        alternativeSourceRemoteDataSourceImpl: AlternativeSourceRemoteDataSourceImpl
    ): AlternativeSourceRemoteDataSource

    @Binds
    abstract fun bindAlternativeSourceRepository(
        alternativeSourceRepositoryImpl: AlternativeSourceRepositoryImpl
    ): AlternativeSourceRepository
}
