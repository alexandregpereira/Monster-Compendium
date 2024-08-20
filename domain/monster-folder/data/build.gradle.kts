plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

multiplatform {
    commonMain {
        implementation(project(":domain:monster:data"))
        implementation(project(":domain:monster-folder:core"))
        implementation(project(":domain:settings:core"))
        implementation(libs.kotlin.coroutines.core)
        implementation(libs.kotlin.datetime)
        implementation(libs.koin.core)
    }

    iosMain {
        implementation(libs.ktor.darwin)
    }
    androidMain()
    jvmMain()
}

android {
    namespace = "br.alexandregpereira.hunter.data.monster.folder"
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()
    }
}
