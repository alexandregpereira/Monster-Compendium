/*
 * Copyright 2023 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
    kotlin("multiplatform")
}

multiplatform {
    commonMain {
        implementation(project(":core:analytics"))
        implementation(project(":core:dynamic-formulary"))
        implementation(project(":core:event"))
        implementation(project(":core:state-holder"))
        implementation(project(":core:localization"))
        implementation(project(":core:uuid"))
        implementation(project(":domain:monster:core"))
        implementation(project(":domain:monster-lore:core"))
        implementation(project(":domain:spell:core"))
        implementation(project(":feature:monster-registration:event"))
        implementation(project(":feature:spell-detail:event"))
        implementation(project(":feature:spell-compendium:event"))
        implementation(libs.kotlin.coroutines.core)
        implementation(libs.koin.core)
    }
    jvmMain()
    iosMain()
}
