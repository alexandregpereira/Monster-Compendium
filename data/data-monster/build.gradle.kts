plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("kapt")
    kotlin("plugin.serialization")
}

kotlin {
    android()
    jvm()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "data-monster"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":domain:alternative-source"))
                implementation(project(":domain:monster"))
                implementation(project(":domain:settings"))
                implementation(libs.kotlin.coroutines.core)
                implementation(libs.kotlin.serialization)
                implementation(libs.koin.core)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.bundles.retrofit)
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

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
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
