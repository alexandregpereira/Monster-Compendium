plugins {
    kotlin("multiplatform")
}

multiplatform {
    commonMain {
        implementation(project(":core:localization"))
        implementation(libs.koin.core)
        implementation(libs.kotlin.coroutines.core)
    }
    jvmMain()
    iosMain()
}
