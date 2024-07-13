package br.alexandregpereira.hunter.monster.event

import org.koin.dsl.module

val monsterEventModule = module {
    single<MonsterEventDispatcher> { MonsterEventDispatcher(get()) }
}
