plugins {
    kotlin("multiplatform")
}

configureJvmTargets()

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":core:analytics"))
                api(project(":core:event"))
                api(project(":core:state-holder"))
                api(project(":domain:monster-compendium:core"))
                implementation(project(":feature:folder-preview:event"))
                implementation(project(":feature:monster-detail:event"))
                implementation(project(":feature:sync:event"))
                implementation(project(":feature:monster-registration:event"))
                implementation(libs.kotlin.coroutines.core)
                implementation(libs.koin.core)
            }
        }
        if (isMac()) {
            val iosMain by getting
        }
    }
}
