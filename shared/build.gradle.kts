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

configureJvmTargets(iosFramework = { linkerOpts("-l", "sqlite3") })

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":core:analytics"))
                implementation(project(":data:data-app"))
                implementation(project(":domain:app"))
                implementation(project(":domain:sync"))
                implementation(project(":feature:folder-insert:event")) // TODO Remove later
                implementation(project(":feature:folder-preview:event")) // TODO Remove later
                implementation(project(":feature:monster-lore-detail:event")) // TODO Remove later
                implementation(project(":feature:spell-detail:event")) // TODO Remove later
                implementation(project(":feature:monster-compendium:state-holder"))
                implementation(project(":feature:monster-detail:state-holder"))
                implementation(project(":feature:sync:state-holder"))
                implementation(libs.kotlin.coroutines.core)
                implementation(libs.koin.core)
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(libs.bundles.unittest)
                implementation(libs.koin.test)
            }
        }
        if (isMac()) {
            val iosMain by getting
        }
    }
}
