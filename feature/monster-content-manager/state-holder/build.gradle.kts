plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

configureTargets()

kotlin {
    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(libs.viewmodel.savedstate)
            }
        }
        val commonMain by getting {
            dependencies {
                implementation(project(":core:analytics"))
                api(project(":core:state-holder"))
                api(project(":domain:alternative-source:core"))
                api(project(":domain:monster:core"))
                api(project(":domain:monster-compendium:core"))
                implementation(project(":feature:monster-content-manager:event"))
                implementation(project(":feature:sync:event"))
                implementation(libs.kotlin.coroutines.core)
                implementation(libs.koin.core)
            }
        }
        if (isMac()) {
            val iosMain by getting
        }
    }
}

android {
    namespace = "br.alexandregpereira.hunter.monster.content.state"
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()
    }
}
