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
    androidMain()

    commonMain {
        implementation(project(":core:analytics"))
        implementation(project(":core:localization"))
        implementation(project(":core:ui:state-recovery"))
        api(project(":core:state-holder"))
        api(project(":domain:alternative-source:core"))
        api(project(":domain:monster:core"))
        api(project(":domain:monster-compendium:core"))
        implementation(project(":feature:monster-content-manager:event"))
        implementation(project(":feature:sync:event"))
        implementation(libs.kotlin.coroutines.core)
        implementation(libs.koin.core)
    }
    jvmMain()
    iosMain()
}

android {
    namespace = "br.alexandregpereira.hunter.monster.content.state"
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()
    }
}
