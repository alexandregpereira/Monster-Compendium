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
                api(project(":domain:monster-folder:core"))
                implementation(project(":feature:folder-insert:event"))
                implementation(libs.kotlin.coroutines.core)
                implementation(libs.koin.core)
            }
        }
        if (isMac()) {
            val iosMain by getting
        }
    }
}
