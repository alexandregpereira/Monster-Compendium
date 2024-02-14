plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization")
}

multiplatform {
    commonMain {
        implementation(project(":domain:settings:core"))
        implementation(project(":domain:spell:core"))
        implementation(libs.kotlin.coroutines.core)
        implementation(libs.kotlin.serialization)
        implementation(libs.koin.core)
        implementation(libs.ktor.core)
    }

    androidMain {
        implementation(libs.ktor.okhttp)
    }

    jvmMain {
        implementation(libs.ktor.okhttp)
    }

    iosMain {
        implementation(libs.ktor.darwin)
    }
}

android {
    namespace = "br.alexandregpereira.hunter.data.spell"
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()
    }
}
