plugins {
    kotlin("multiplatform")
    alias(libs.plugins.compose.compiler)
}

multiplatform {
    commonMain {
        implementation(project(":feature:monster-compendium:state-holder"))
        implementation(project(":ui:core"))
        implementation(project(":ui:monster-compendium"))

        implementation(libs.koin.compose)
        implementation(libs.kotlin.reflect)
        implementation(libs.kotlin.coroutines.core)
    }
    jvmMain()
    iosMain()
}

composeCompiler {
    enableStrongSkippingMode = true
}