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

configureJvmTargets()

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":core:analytics"))
                api(project(":core:state-holder"))
                api(project(":domain:monster"))
                implementation(project(":domain:monster-lore"))
                implementation(project(":domain:spell"))
                implementation(project(":feature:folder-insert:event"))
                implementation(project(":feature:monster-detail:event"))
                implementation(project(":feature:monster-lore-detail:event"))
                implementation(project(":feature:spell-detail:event"))
                implementation(libs.kotlin.coroutines.core)
                implementation(libs.koin.core)
            }
        }
        if (isMac()) {
            val iosMain by getting
        }
    }
}
