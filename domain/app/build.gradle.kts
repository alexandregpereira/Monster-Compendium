plugins {
    kotlin("multiplatform")
}

configureJvmTargets()

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":domain:alternative-source"))
                implementation(project(":domain:monster"))
                implementation(project(":domain:monster-folder"))
                implementation(project(":domain:monster-lore"))
                implementation(project(":domain:settings"))
                implementation(project(":domain:spell"))
                implementation(project(":domain:sync"))
                implementation(libs.koin.core)
            }
        }
        if (isMac()) {
            val iosMain by getting
        }
    }
}
