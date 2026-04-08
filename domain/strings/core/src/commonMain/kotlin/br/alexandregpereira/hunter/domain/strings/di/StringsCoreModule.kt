package br.alexandregpereira.hunter.domain.strings.di

import br.alexandregpereira.hunter.domain.strings.SyncStringsUseCase
import org.koin.dsl.module

val stringsCoreModule = module {
    factory { SyncStringsUseCase(get()) }
}
