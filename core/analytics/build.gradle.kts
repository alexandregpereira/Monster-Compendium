plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

configureTargets()

kotlin {
    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(project.dependencies.platform(libs.firebase.bom))
                implementation(libs.firebase.analytics)
                implementation(libs.firebase.crashlytics)
            }
        }
        val commonMain by getting {
            dependencies {
                implementation(libs.koin.core)
                implementation(libs.kotlin.coroutines.core)
            }
        }
        if (isMac()) {
            val iosMain by getting
        }
    }
}

android {
    namespace = "br.alexandregpereira.hunter.analytics"
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()
    }
}
