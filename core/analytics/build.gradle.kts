/*
 * Copyright (c) 2024 Alexandre Gomes Pereira
 *
 * SPDX-License-Identifier: MIT
 *
 * Licensed under the MIT License. See the LICENSE file in this module directory
 * for the full license text.
 */

plugins {
    kotlin("multiplatform")
    id("com.android.kotlin.multiplatform.library")
    kotlin("native.cocoapods")
}

multiplatform {
    commonMain {
        implementation(libs.koin.core)
        implementation(libs.kotlin.coroutines.core)
    }

    androidMain("br.alexandregpereira.hunter.analytics") {
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
        // A dynamic framework links its pods with ld, which cannot see the pods they depend on
        // (e.g. FirebaseCore under FirebaseAnalytics, or AmplitudeSwift from core:analytics).
        // The app consumes a static framework and lets Xcode link every pod from the Podfile.
        framework {
            isStatic = true
        }

        pod("AmplitudeSwift") {
            version = "~> 1.10"
        }
        pod("FirebaseAnalytics")
        pod("FirebaseCrashlytics")
    }
}
