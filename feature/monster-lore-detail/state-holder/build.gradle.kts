plugins {
    kotlin("multiplatform")
}

configureJvmTargets()

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":core:analytics"))
                api(project(":core:state-holder"))
                api(project(":domain:monster-lore:core"))
                implementation(project(":domain:monster:core"))
                implementation(project(":feature:monster-lore-detail:event"))
                implementation(libs.kotlin.coroutines.core)
                implementation(libs.koin.core)
            }
        }
        if (isMac()) {
            val iosMain by getting
        }
    }
}
