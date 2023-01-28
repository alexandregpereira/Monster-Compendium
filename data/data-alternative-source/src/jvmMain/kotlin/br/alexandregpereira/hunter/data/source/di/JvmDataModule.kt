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

import br.alexandregpereira.hunter.data.source.remote.AlternativeSourceApi
import br.alexandregpereira.hunter.data.source.remote.AlternativeSourceRemoteDataSource
import br.alexandregpereira.hunter.data.source.remote.JvmAlternativeSourceRemoteDataSource
import org.koin.core.module.Module
import org.koin.core.scope.Scope
import org.koin.dsl.module
import retrofit2.Retrofit

internal actual fun getAdditionalModule(): Module {
    return module {
        factory<AlternativeSourceApi> { get<Retrofit>().create(AlternativeSourceApi::class.java) }
    }
}

internal actual fun Scope.createRemoteDataSource(): AlternativeSourceRemoteDataSource? {
    return JvmAlternativeSourceRemoteDataSource(get())
}
