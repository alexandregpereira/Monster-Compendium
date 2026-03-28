/*
 * Copyright (C) 2026 Alexandre Gomes Pereira
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
    alias(libs.plugins.compose.compiler)
}

multiplatform {
    androidMain {
        implementation(libs.revenuecat.kmp.core)
        implementation(libs.revenuecat.kmp.ui)
    }
    commonMain {
        implementation(project(":core:analytics"))
        implementation(project(":core:event"))
        implementation(project(":core:localization"))
        implementation(project(":core:network"))
        implementation(project(":core:state-holder"))
        implementation(project(":core:ui:state-recovery"))
        implementation(project(":domain:revenue:core"))
        implementation(project(":feature:folder-detail:state-holder"))
        implementation(project(":feature:revenue:event"))
        implementation(project(":ui:core"))

        implementation(libs.kotlin.coroutines.core)
        implementation(libs.koin.compose)
    }
    jvmMain()
    iosMain {
        implementation(libs.revenuecat.kmp.core)
        implementation(libs.revenuecat.kmp.ui)
    }
}

androidLibrary {
    namespace = "br.alexandregpereira.hunter.revenue"
}
