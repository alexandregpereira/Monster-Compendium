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
    kotlin("plugin.serialization")
    alias(libs.plugins.sqldelight)
}

multiplatform {
    commonMain {
        implementation(project(":domain:alternative-source:data"))
        implementation(project(":domain:monster:data"))
        implementation(project(":domain:monster-folder:data"))
        implementation(project(":domain:monster-lore:data"))
        implementation(project(":domain:settings:data"))
        implementation(project(":domain:spell:data"))
        implementation(libs.kotlin.coroutines.core)
        implementation(libs.kotlin.serialization)
        implementation(libs.koin.core)
        implementation(libs.ktor.core)
        implementation(libs.ktor.logging)
    }

    androidMain {
        implementation(libs.ktor.okhttp)
        implementation(libs.sqldelight.android)
    }

    jvmMain {
        implementation(libs.ktor.okhttp)
        implementation(libs.sqldelight.jvm)
    }

    iosMain {
        implementation(libs.ktor.darwin)
        implementation(libs.sqldelight.ios)
    }
}

android {
    namespace = "br.alexandregpereira.hunter.data"
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()
    }
}

sqldelight {
    database("Database") {
        packageName = "br.alexandregpereira.hunter.data"
        schemaOutputDirectory = file("src/commonMain/sqldelight/databases")
    }
}
