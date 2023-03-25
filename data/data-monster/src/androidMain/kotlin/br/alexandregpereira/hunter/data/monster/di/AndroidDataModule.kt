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

import br.alexandregpereira.hunter.data.monster.preferences.AndroidPreferencesDataSource
import br.alexandregpereira.hunter.data.monster.preferences.PreferencesDataSource
import br.alexandregpereira.hunter.domain.repository.MonsterLocalRepository
import org.koin.core.module.Module
import org.koin.core.scope.Scope
import org.koin.dsl.module

internal actual fun getAdditionalModule(): Module {
    return module {  }
}

internal actual fun Scope.createPreferencesDataSource(): PreferencesDataSource? {
    return AndroidPreferencesDataSource(get())
}

internal actual fun Scope.createMonsterLocalRepository(): MonsterLocalRepository? {
    return null
}
