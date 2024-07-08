plugins {
    kotlin("multiplatform")
    alias(libs.plugins.compose.compiler)
}

multiplatform {
    commonMain {
        implementation(project(":feature:sync:state-holder"))
        implementation(project(":ui:core"))

        implementation(libs.kotlin.coroutines.core)
        implementation(libs.koin.compose)
    }
    jvmMain()
    iosMain()
}

composeCompiler {
    enableStrongSkippingMode = true
}
