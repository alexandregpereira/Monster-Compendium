package br.alexandregpereira.hunter.data.monster.remote

import org.koin.dsl.module

val remoteDataSourceModule = module {
    single<MonsterRemoteDataSource> { MonsterRemoteDataSourceImpl() }
}