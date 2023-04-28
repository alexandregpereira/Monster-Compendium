plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization")
}

configureTargets()

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":domain:alternative-source"))
                implementation(project(":domain:monster"))
                implementation(project(":domain:settings"))
                implementation(libs.kotlin.coroutines.core)
                implementation(libs.kotlin.serialization)
                implementation(libs.koin.core)
                implementation(libs.ktor.core)
            }
        }
        val androidMain by getting {
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

android {
    namespace = "br.alexandregpereira.hunter.data.monster"
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()
    }
}
