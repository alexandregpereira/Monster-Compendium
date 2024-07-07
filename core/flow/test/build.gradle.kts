plugins {
    kotlin("multiplatform")
}

multiplatform {
    commonMain {
        implementation(kotlin("test"))
        implementation(libs.kotlin.coroutines.test)
    }
    jvmMain()
    iosMain()
}
