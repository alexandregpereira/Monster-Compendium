plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization")
    alias(libs.plugins.sqldelight)
}

configureTargets()

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":domain:alternative-source:data"))
                implementation(project(":domain:monster:data"))
                implementation(project(":domain:monster-folder:data"))
                implementation(project(":domain:monster-lore:data"))
                implementation(project(":domain:settings:data"))
                implementation(project(":domain:spell:data"))
                implementation(libs.kotlin.coroutines.core)
                implementation(libs.kotlin.serialization)
                implementation(libs.koin.core)
                implementation(libs.ktor.core)
                implementation(libs.ktor.logging)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.ktor.okhttp)
                implementation(libs.sqldelight.android)
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(libs.ktor.okhttp)
                implementation(libs.sqldelight.jvm)
            }
        }
        if (isMac()) {
            val iosMain by getting {
                dependencies {
                    implementation(libs.ktor.darwin)
                    implementation(libs.sqldelight.ios)
                }
            }
        }
    }
}

android {
    namespace = "br.alexandregpereira.hunter.data"
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()
    }
}

sqldelight {
    database("Database") {
        packageName = "br.alexandregpereira.hunter.data"
        schemaOutputDirectory = file("src/commonMain/sqldelight/databases")
    }
}
