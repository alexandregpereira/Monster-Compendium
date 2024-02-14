plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization")
}

multiplatform {
    commonMain {
        implementation(project(":core:uuid"))
        implementation(project(":domain:alternative-source:core"))
        implementation(project(":domain:monster:core"))
        implementation(project(":domain:settings:core"))
        implementation(libs.kotlin.coroutines.core)
        implementation(libs.kotlin.serialization)
        implementation(libs.koin.core)
        implementation(libs.ktor.core)
    }

    androidMain {
        implementation(libs.ktor.okhttp)
    }

    iosMain {
        implementation(libs.ktor.darwin)
    }
    jvmMain()
}

android {
    namespace = "br.alexandregpereira.hunter.data.monster"
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()
    }
}
