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
    id("com.android.kotlin.multiplatform.library")
    kotlin("multiplatform")
    alias(libs.plugins.compose.compiler)
}

multiplatform {
    androidMain("br.alexandregpereira.hunter.home")
    commonMain {
        implementation(project(":core:analytics"))
        implementation(project(":core:event"))
        implementation(project(":core:localization"))
        implementation(project(":core:state-holder"))
        implementation(project(":domain:alternative-source:core"))
        implementation(project(":domain:monster:core"))
        implementation(project(":domain:monster-folder:core"))
        implementation(project(":domain:monster:event"))
        implementation(project(":domain:spell:core"))
        implementation(project(":feature:folder-detail:event"))
        implementation(project(":feature:folder-list:event"))
        implementation(project(":feature:home:event"))
        implementation(project(":feature:monster-compendium:event"))
        implementation(project(":feature:monster-content-manager:event"))
        implementation(project(":feature:monster-registration:event"))
        implementation(project(":feature:search:event"))
        implementation(project(":feature:settings:event"))
        implementation(project(":feature:spell-compendium:event"))
        implementation(project(":feature:spell-detail:event"))
        implementation(project(":feature:spell-registration:event"))
        implementation(project(":ui:core"))
        implementation(project(":ui:monster-compendium"))

        implementation(libs.kotlin.coroutines.core)
        implementation(libs.koin.compose)
    }
    commonTest {
        implementation(kotlin("test"))
    }
    jvmMain()
    iosMain()
}
