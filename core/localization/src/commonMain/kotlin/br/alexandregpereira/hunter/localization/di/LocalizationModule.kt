package br.alexandregpereira.hunter.localization.di

import br.alexandregpereira.hunter.localization.AppLocalization
import br.alexandregpereira.hunter.localization.AppLocalizationImpl
import br.alexandregpereira.hunter.localization.AppReactiveLocalization
import br.alexandregpereira.hunter.localization.Language
import br.alexandregpereira.hunter.localization.MutableAppLocalization
import br.alexandregpereira.hunter.localization.getDeviceLangCode
import org.koin.dsl.module

val localizationModule = module {
    factory<Language> { getDefaultLanguage() }
    single { AppLocalizationImpl(get()) }
    single<MutableAppLocalization> { get<AppLocalizationImpl>() }
    single<AppLocalization> { get<AppLocalizationImpl>() }
    single<AppReactiveLocalization> { get<AppLocalizationImpl>() }
}

private fun getDefaultLanguage(): Language {
    return Language.entries.firstOrNull {
        it.code == getDeviceLangCode()
    } ?: Language.ENGLISH
}
