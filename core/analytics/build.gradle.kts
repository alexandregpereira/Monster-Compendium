plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

multiplatform {
    commonMain {
        implementation(libs.koin.core)
        implementation(libs.kotlin.coroutines.core)
    }

    androidMain {
        implementation(project.dependencies.platform(libs.firebase.bom))
        implementation(libs.firebase.analytics)
        implementation(libs.firebase.crashlytics)
    }
    jvmMain()
    iosMain()
}

android {
    namespace = "br.alexandregpereira.hunter.analytics"
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()
    }
}
