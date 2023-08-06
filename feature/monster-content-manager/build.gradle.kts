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
                api(project(":domain:alternative-source"))
                api(project(":domain:monster"))
                api(project(":domain:monster-compendium"))
                implementation(project(":event:monster-content-manager-event"))
                implementation(project(":event:sync-event"))
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
