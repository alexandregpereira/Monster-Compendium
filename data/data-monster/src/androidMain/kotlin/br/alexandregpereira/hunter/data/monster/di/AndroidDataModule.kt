/*
 * Copyright 2023 Alexandre Gomes Pereira
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

package br.alexandregpereira.hunter.data.monster.di

import br.alexandregpereira.hunter.data.monster.AndroidMonsterLocalRepository
import br.alexandregpereira.hunter.data.monster.local.MonsterLocalDataSource
import br.alexandregpereira.hunter.data.monster.local.MonsterLocalDataSourceImpl
import br.alexandregpereira.hunter.data.monster.preferences.AndroidPreferencesDataSource
import br.alexandregpereira.hunter.data.monster.preferences.PreferencesDataSource
import br.alexandregpereira.hunter.data.monster.remote.AndroidMonsterRemoteDataSource
import br.alexandregpereira.hunter.data.monster.remote.MonsterApi
import br.alexandregpereira.hunter.data.monster.remote.MonsterRemoteDataSource
import br.alexandregpereira.hunter.data.monster.remote.MonsterRemoteDataSourceErrorHandler
import br.alexandregpereira.hunter.domain.repository.MonsterLocalRepository
import org.koin.core.module.Module
import org.koin.core.scope.Scope
import org.koin.dsl.module
import retrofit2.Retrofit

actual fun getAdditionalModule(): Module {
    return module {
        factory<MonsterApi> { get<Retrofit>().create(MonsterApi::class.java) }
        single<MonsterLocalDataSource> {
            MonsterLocalDataSourceImpl(
                abilityScoreDao = get(),
                actionDao = get(),
                conditionDao = get(),
                damageDao = get(),
                damageDiceDao = get(),
                monsterDao = get(),
                savingThrowDao = get(),
                skillDao = get(),
                specialAbilityDao = get(),
                speedDao = get(),
                speedValueDao = get(),
                reactionDao = get(),
                spellcastingDao = get(),
                spellUsageDao = get(),
                legendaryActionDao = get()
            )
        }
        single { AndroidMonsterRemoteDataSource(get()) }
    }
}

actual fun Scope.createRemoteDataSource(): MonsterRemoteDataSource? {
    return get<AndroidMonsterRemoteDataSource>()
}

actual fun Scope.createRemoteDataSourceErrorHandler(): MonsterRemoteDataSourceErrorHandler? {
    return get<AndroidMonsterRemoteDataSource>()
}

actual fun Scope.createPreferencesDataSource(): PreferencesDataSource? {
    return AndroidPreferencesDataSource(get())
}

actual fun Scope.createMonsterLocalRepository(): MonsterLocalRepository? {
    return AndroidMonsterLocalRepository(get())
}
