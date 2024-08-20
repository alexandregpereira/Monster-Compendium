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
    alias(libs.plugins.compose.compiler)
}

multiplatform {
    commonMain {
        implementation(project(":feature:monster-compendium:state-holder"))
        implementation(project(":ui:core"))
        implementation(project(":ui:monster-compendium"))

        implementation(libs.koin.compose)
        implementation(libs.kotlin.reflect)
        implementation(libs.kotlin.coroutines.core)
    }
    jvmMain()
    iosMain()
}

composeCompiler {
    enableStrongSkippingMode = true
}