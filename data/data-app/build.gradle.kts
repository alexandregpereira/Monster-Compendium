plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("kapt")
    kotlin("plugin.serialization")
}

configureTargets()

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":data:data-alternative-source"))
                implementation(project(":data:data-monster"))
                implementation(project(":data:data-monster-folder"))
                implementation(project(":data:data-monster-lore"))
                implementation(project(":data:data-settings"))
                implementation(project(":data:data-spell"))
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
                implementation(libs.bundles.room)
                configurations["kapt"]
                    .dependencies
                    .add(project.dependencies.create(libs.room.compiler.get().toString()))
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(libs.bundles.unittest)
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

android {
    namespace = "br.alexandregpereira.hunter.data"
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()

        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.schemaLocation"] = "$projectDir/schemas"
            }
        }
    }
}
