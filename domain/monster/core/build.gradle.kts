plugins {
    kotlin("multiplatform")
}

multiplatform {
    commonMain {
        implementation(project(":core:uuid"))
        implementation(libs.koin.core)
        implementation(libs.kotlin.coroutines.core)
    }

    jvmTest {
        implementation(libs.bundles.unittest)
    }
    jvmMain()
    iosMain()
}
