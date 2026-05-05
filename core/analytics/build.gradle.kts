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

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("native.cocoapods")
}

multiplatform {
    commonMain {
        implementation(libs.koin.core)
        implementation(libs.kotlin.coroutines.core)
    }

    androidMain {
        implementation(project.dependencies.platform(libs.firebase.bom))
        implementation(libs.firebase.analytics)
        implementation(libs.firebase.crashlytics)
        implementation(libs.amplitude.android)
    }
    jvmMain {
        implementation(libs.amplitude.jvm)
        implementation(libs.json.jvm)
    }
    iosMain()
}

kotlin {
    cocoapods {
        version = "1.0"
        summary = "Analytics module"
        homepage = "https://github.com/alexandregpereira/monster-compendium"
        ios.deploymentTarget = "14.0"

        pod("AmplitudeSwift") {
            version = "~> 1.10"
        }
        pod("FirebaseAnalytics")
        pod("FirebaseCrashlytics")
    }
}

android {
    namespace = "br.alexandregpereira.hunter.analytics"
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()
    }
    buildFeatures {
        buildConfig = true
    }
}
