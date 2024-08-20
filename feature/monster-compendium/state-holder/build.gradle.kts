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
}

multiplatform {
    commonMain {
        implementation(project(":core:analytics"))
        implementation(project(":core:event"))
        implementation(project(":core:localization"))
        api(project(":core:state-holder"))
        implementation(project(":domain:monster:event"))
        api(project(":domain:monster-compendium:core"))
        implementation(project(":domain:sync:core"))
        implementation(project(":feature:folder-preview:event"))
        implementation(project(":feature:sync:event"))
        implementation(project(":feature:monster-registration:event"))
        implementation(libs.kotlin.coroutines.core)
        implementation(libs.koin.core)
    }
    commonTest {
        implementation(kotlin("test"))
        implementation(project(":core:flow:test"))
        implementation(libs.kotlin.coroutines.test)
    }
    jvmMain()
    iosMain()
}
