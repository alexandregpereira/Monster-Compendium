/*
 * Copyright (C) 2024 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.app

import android.app.Application
import br.alexandregpereira.hunter.ads.consent.AdsConsentManager
import br.alexandregpereira.hunter.app.di.initKoinModules
import br.alexandregpereira.hunter.featureFlag.FeatureFlagProvider
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import com.google.firebase.crashlytics.crashlytics
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.module

class HunterApplication : Application() {

    private val adsConsentManager: AdsConsentManager by inject()
    private val featureFlagProvider: FeatureFlagProvider by inject()

    override fun onCreate() {
        super.onCreate()
        initKoin()
        featureFlagProvider.initialize()
        adsConsentManager.initialize()
    }

    private fun initKoin() {
        startKoin {
            initAndroidModules(this@HunterApplication)
        }
    }
}

internal fun KoinApplication.initAndroidModules(app: Application) {
    androidContext(app)
    modules(
        module {
            factory { Firebase.analytics }
            factory { Firebase.crashlytics }
        },
    )
    initKoinModules()
}
