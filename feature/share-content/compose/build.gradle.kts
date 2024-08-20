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
    kotlin("plugin.serialization")
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

multiplatform {
    androidMain()
    commonMain {
        implementation(project(":domain:monster:core"))
        implementation(project(":domain:monster-lore:core"))
        implementation(project(":domain:spell:core"))
        implementation(project(":core:localization"))
        implementation(project(":core:analytics"))
        implementation(project(":core:event"))
        implementation(project(":core:state-holder"))
        implementation(project(":feature:share-content:event"))
        implementation(project(":ui:core"))

        implementation(libs.kotlin.coroutines.core)
        implementation(libs.koin.compose)
        implementation(libs.kotlin.serialization)
    }
    jvmMain()
    jvmTest {
        implementation(kotlin("test"))
        implementation(libs.kotlin.coroutines.test)
    }
    iosMain()
}

androidLibrary {
    namespace = "br.alexandregpereira.hunter.shareContent"
}

composeCompiler {
    enableStrongSkippingMode = true
}

compose.resources {
    publicResClass = false
    packageOfResClass = "br.alexandregpereira.hunter.shareContent.ui.resources"
    generateResClass = always
}
