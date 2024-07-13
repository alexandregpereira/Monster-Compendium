plugins {
    kotlin("multiplatform")
}

multiplatform {
    commonMain {
        implementation(libs.kotlin.coroutines.core)
    }
    jvmMain()
    iosMain()
}
