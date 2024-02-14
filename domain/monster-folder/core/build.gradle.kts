plugins {
    kotlin("multiplatform")
}

multiplatform {
    commonMain {
        implementation(project(":domain:monster:core"))
        implementation(libs.koin.core)
        implementation(libs.kotlin.coroutines.core)
    }
    jvmMain()
    iosMain()
}
