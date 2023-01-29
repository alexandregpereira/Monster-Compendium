plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

configureTargets()

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":domain:settings"))
                implementation(libs.kotlin.coroutines.core)
                implementation(libs.koin.core)
                implementation(libs.ktor.core)
            }
        }
        val androidMain by getting
        val jvmMain by getting
        if (isMac()) {
            val iosMain by getting
        }
    }
}

android {
    namespace = "br.alexandregpereira.hunter.data.settings"
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()
    }
}
