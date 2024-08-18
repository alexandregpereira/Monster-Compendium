plugins {
    kotlin("multiplatform")
}

multiplatform {
    commonMain {
        api(project(":core:event"))
        implementation(libs.koin.core)
        implementation(libs.kotlin.coroutines.core)
    }
    jvmMain()
    iosMain()
}
