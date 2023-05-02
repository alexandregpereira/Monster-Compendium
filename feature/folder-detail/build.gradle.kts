plugins {
    kotlin("multiplatform")
}

configureJvmTargets()

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":core:state-holder"))
                api(project(":domain:monster-folder"))
                implementation(project(":event:folder-preview-event"))
                implementation(project(":event:folder-detail-event"))
                implementation(project(":event:folder-insert-event"))
                implementation(project(":event:monster-detail-event"))
                implementation(libs.kotlin.coroutines.core)
                implementation(libs.koin.core)
            }
        }
        if (isMac()) {
            val iosMain by getting
        }
    }
}
