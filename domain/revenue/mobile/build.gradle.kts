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
        implementation(project(":core:analytics"))
        implementation(project(":core:feature-flag"))
        implementation(project(":domain:revenue:core"))
        implementation(libs.multiplatform.settings)
        implementation(libs.koin.core)
        implementation(libs.kotlin.coroutines.core)
        implementation(libs.kotlin.serialization)
        implementation(libs.ktor.core)
        implementation(libs.revenuecat.kmp.core)
    }
    androidMain {
        implementation(libs.ktor.okhttp)
    }
    iosMain {
        implementation(libs.ktor.darwin)
    }
}

kotlin {
    cocoapods {
        name = "revenue_mobile"
        version = "1.0"
        summary = "Revenue mobile module"
        homepage = "https://github.com/alexandregpereira/monster-compendium"
        ios.deploymentTarget = "14.0"
        pod("PurchasesHybridCommon") {
            version = "17.46.0"
        }
    }
}

android {
    namespace = "br.alexandregpereira.hunter.revenue.mobile"
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()
    }
}
