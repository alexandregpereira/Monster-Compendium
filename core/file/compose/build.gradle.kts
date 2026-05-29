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
    id("org.jetbrains.compose")
    alias(libs.plugins.compose.compiler)
}
multiplatform {
    commonMain {
        api(project(":core:file:core"))
        implementation(libs.compose.mp.runtime)
        implementation(libs.filekit.compose)
        implementation(libs.kotlin.coroutines.core)
    }
    androidMain("br.alexandregpereira.file.compose")
    jvmMain()
    iosMain()
}
