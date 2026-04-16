package br.alexandregpereira.hunter.spell.event

import org.koin.dsl.module

val spellEventModule = module {
    single<SpellResultDispatcher> {
        SpellResultDispatcher()
    }
}
