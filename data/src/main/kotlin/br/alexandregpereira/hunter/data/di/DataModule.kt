/*
 * Copyright (c) 2021 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.alexandregpereira.hunter.data.di

import br.alexandregpereira.hunter.data.MonsterRepositoryImpl
import br.alexandregpereira.hunter.data.remote.FileManager
import br.alexandregpereira.hunter.data.remote.FileManagerImpl
import br.alexandregpereira.hunter.data.remote.MonsterRemoteDataSource
import br.alexandregpereira.hunter.data.remote.MonsterRemoteDataSourceImpl
import br.alexandregpereira.hunter.domain.MonsterRepository
import org.koin.core.module.Module
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

private const val JSON_FORMATTED_FILE_NAME = "json/monsters.json"

val remoteDataSourceModule = module {
    single<MonsterRemoteDataSource> {
        MonsterRemoteDataSourceImpl(get(qualifier(JSON_FORMATTED_FILE_NAME)))
    }
    single<FileManager>(qualifier = qualifier(JSON_FORMATTED_FILE_NAME)) {
        FileManagerImpl(JSON_FORMATTED_FILE_NAME)
    }
}

val repositoryModule = listOf(
    module {
        single<MonsterRepository> {
            MonsterRepositoryImpl(get())
        }
    },
    remoteDataSourceModule
)
