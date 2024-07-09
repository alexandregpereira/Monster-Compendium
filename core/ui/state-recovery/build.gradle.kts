plugins {
    kotlin("multiplatform")
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

multiplatform {
    commonMain {
        implementation(compose.runtimeSaveable)
        implementation(libs.kotlin.coroutines.core)
    }
    jvmMain()
    iosMain()
}
