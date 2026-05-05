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
}

multiplatform {
    commonMain {
        implementation(project(":core:analytics"))
        implementation(libs.koin.core)
        implementation(libs.kotlin.coroutines.core)
    }
    androidMain {
        implementation(libs.play.services.ads)
        implementation(libs.user.messaging.platform.android)
    }
    jvmMain()
    iosMain()
}

android {
    namespace = "br.alexandregpereira.hunter.ads.consent"
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()
    }
}
