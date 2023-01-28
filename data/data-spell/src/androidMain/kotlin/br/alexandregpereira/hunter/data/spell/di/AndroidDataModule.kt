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

import br.alexandregpereira.hunter.data.spell.AndroidSettingsSpellDataRepository
import br.alexandregpereira.hunter.data.spell.AndroidSpellRepository
import br.alexandregpereira.hunter.data.spell.local.SpellLocalDataSource
import br.alexandregpereira.hunter.data.spell.local.SpellLocalDataSourceImpl
import br.alexandregpereira.hunter.data.spell.remote.AndroidSpellRemoteDataSource
import br.alexandregpereira.hunter.data.spell.remote.SpellApi
import br.alexandregpereira.hunter.data.spell.remote.SpellRemoteDataSource
import br.alexandregpereira.hunter.domain.settings.SettingsSpellDataRepository
import br.alexandregpereira.hunter.domain.spell.SpellRepository
import org.koin.core.module.Module
import org.koin.core.scope.Scope
import org.koin.dsl.module
import retrofit2.Retrofit

internal actual fun getAdditionalModule(): Module {
    return module {
        factory<SpellLocalDataSource> { SpellLocalDataSourceImpl(get()) }
        single { get<Retrofit>().create(SpellApi::class.java) }
    }
}

internal actual fun Scope.createRepository(): SpellRepository? {
    return AndroidSpellRepository(get(), get())
}

internal actual fun Scope.createSettingsRepository(): SettingsSpellDataRepository? {
    return AndroidSettingsSpellDataRepository(get())
}

internal actual fun Scope.createRemoteDataSource(): SpellRemoteDataSource? {
    return AndroidSpellRemoteDataSource(get())
}
