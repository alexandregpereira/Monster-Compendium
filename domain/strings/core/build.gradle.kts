plugins {
    kotlin("multiplatform")
}

multiplatform {
    commonMain {
        implementation(libs.koin.core)
        implementation(libs.kotlin.coroutines.core)
    }
    jvmMain()
    iosMain()
}
