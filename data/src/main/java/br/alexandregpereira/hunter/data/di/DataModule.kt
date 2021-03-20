package br.alexandregpereira.hunter.data.di

import br.alexandregpereira.hunter.data.monster.remote.FileManager
import br.alexandregpereira.hunter.data.monster.remote.FileManagerImpl
import br.alexandregpereira.hunter.data.monster.remote.MonsterRemoteDataSource
import br.alexandregpereira.hunter.data.monster.remote.MonsterRemoteDataSourceImpl
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
