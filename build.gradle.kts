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

buildscript {
    extra.apply {
        set("compileSdk", 35)
        set("minSdk", 24)
        set("targetSdk", 35)
    }
    repositories {
        google()
        mavenCentral()
        // TODO Remove when coil is not alpha anymore
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven( "https://androidx.dev/storage/compose-compiler/repository")
        maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev")
    }
    dependencies {
        classpath(libs.gradle.android)
        classpath(libs.gradle.kotlin)
        classpath(libs.gradle.googleplay.services)
        classpath(libs.gradle.firebase.crashlytics)
    }
}

plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.compose.compiler) apply false
}
