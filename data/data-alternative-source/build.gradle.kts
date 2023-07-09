plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

configureJvmTargets()

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":domain:alternative-source"))
                implementation(project(":domain:settings"))
                implementation(libs.kotlin.coroutines.core)
                implementation(libs.kotlin.datetime)
                implementation(libs.kotlin.serialization)
                implementation(libs.koin.core)
                implementation(libs.ktor.core)
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(libs.ktor.okhttp)
            }
        }

        if (isMac()) {
            val iosMain by getting {
                dependencies {
                    implementation(libs.ktor.darwin)
                }
            }
        }
    }
}
