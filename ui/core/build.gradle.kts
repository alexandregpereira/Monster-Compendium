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
    id("com.android.library")
    kotlin("multiplatform")
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

multiplatform {
    androidMain {
        implementation(libs.core.ktx)
        implementation(libs.compose.activity)
        implementation(libs.kotlin.reflect)
        implementation(libs.ktor.android)
    }
    commonMain {
        api(libs.compose.mp.ui)
        api(libs.compose.mp.material)
        api(libs.compose.mp.material.icons.extended)
        api(libs.compose.mp.ui.util)
        api(libs.compose.mp.components.resources)
        api(libs.compose.mp.ui.tooling.preview)
        implementation(libs.ktor.core)
        implementation(libs.coil.compose)
        implementation(libs.coil.mp)
        implementation(libs.coil.network.ktor)
    }
    jvmMain {
        implementation(libs.ktor.jvm)
    }
    iosMain {
        implementation(libs.ktor.darwin)
    }
}

androidLibrary {
    namespace = "br.alexandregpereira.hunter.ui"
}

dependencies {
    debugImplementation(libs.compose.mp.ui.tooling)
}

compose.resources {
    publicResClass = true
    packageOfResClass = "br.alexandregpereira.hunter.ui.resources"
    generateResClass = always
}
