package br.alexandregpereira.hunter.ads.di

import br.alexandregpereira.hunter.ads.AdsConsentManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val androidAdsModule = module {
    single { AdsConsentManager(androidContext()) }
}
