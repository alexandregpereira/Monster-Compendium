plugins {
    kotlin("multiplatform")
}

configureJvmTargets()

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":core:state-holder"))
                api(project(":domain:monster-compendium"))
                implementation(project(":event:folder-preview-event"))
                implementation(project(":event:monster-detail-event"))
                implementation(project(":event:sync-event"))
                implementation(libs.kotlin.coroutines.core)
                implementation(libs.koin.core)
            }
        }
        if (isMac()) {
            val iosMain by getting
        }
    }
}
