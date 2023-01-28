plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "monster-lore"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":domain:monster"))
                implementation(project(":domain:monster-lore"))
                implementation(project(":domain:alternative-source"))
                implementation(project(":domain:spell"))
                implementation(project(":domain:settings"))
                implementation(libs.koin.core)
                implementation(libs.kotlin.coroutines.core)
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
