package br.alexandregpereira.hunter.localization.di

import br.alexandregpereira.hunter.localization.AppLocalization
import br.alexandregpereira.hunter.localization.AppLocalizationImpl
import br.alexandregpereira.hunter.localization.AppReactiveLocalization
import br.alexandregpereira.hunter.localization.MutableAppLocalization
import org.koin.dsl.module

val localizationModule = module {
    single { AppLocalizationImpl() }
    single<MutableAppLocalization> { get<AppLocalizationImpl>() }
    single<AppLocalization> { get<AppLocalizationImpl>() }
    single<AppReactiveLocalization> { get<AppLocalizationImpl>() }
}
