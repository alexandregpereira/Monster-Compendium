plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

multiplatform {
    commonMain {
        implementation(project(":domain:settings:core"))
        implementation(libs.multiplatform.settings)
        implementation(libs.kotlin.coroutines.core)
        implementation(libs.koin.core)
        implementation(libs.ktor.core)
    }
    androidMain()
    jvmMain()
    iosMain()
}

android {
    namespace = "br.alexandregpereira.hunter.data.settings"
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()
    }
}
