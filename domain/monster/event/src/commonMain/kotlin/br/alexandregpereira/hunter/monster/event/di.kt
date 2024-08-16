package br.alexandregpereira.hunter.monster.event

import br.alexandregpereira.hunter.monster.registration.event.MonsterRegistrationEventListener
import org.koin.dsl.module

val monsterEventModule = module {
    single<MonsterEventDispatcher> {
        MonsterEventDispatcher(get(), get<MonsterRegistrationEventListener>())
    }
}
