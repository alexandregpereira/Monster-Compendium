plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

configureTargets()

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":data:data-monster"))
                implementation(project(":domain:monster-folder"))
                implementation(libs.kotlin.coroutines.core)
                implementation(libs.kotlin.datetime)
                implementation(libs.koin.core)
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
    namespace = "br.alexandregpereira.hunter.data.monster.folder"
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()
    }
}
