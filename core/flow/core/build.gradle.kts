plugins {
    kotlin("multiplatform")
}

multiplatform {
    commonMain {
        api(libs.kotlin.coroutines.core)
    }
    jvmMain()
    iosMain()
}
