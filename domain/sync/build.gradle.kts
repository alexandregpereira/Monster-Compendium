plugins {
    kotlin("multiplatform")
}

configureJvmTargets()

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":domain:monster"))
                implementation(project(":domain:monster-lore"))
                implementation(project(":domain:alternative-source"))
                implementation(project(":domain:spell"))
                implementation(project(":domain:settings"))
                implementation(libs.koin.core)
                implementation(libs.kotlin.coroutines.core)
            }
        }
        if (isMac()) {
            val iosMain by getting
        }
    }
}
