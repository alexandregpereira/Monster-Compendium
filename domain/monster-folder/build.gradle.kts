plugins {
    kotlin("multiplatform")
}

configureJvmTargets()

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":domain:monster"))
                implementation(libs.koin.core)
                implementation(libs.kotlin.coroutines.core)
            }
        }
        if (isMac()) {
            val iosMain by getting
        }
    }
}
