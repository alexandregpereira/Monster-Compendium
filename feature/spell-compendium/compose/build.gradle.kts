plugins {
    kotlin("multiplatform")
    alias(libs.plugins.compose.compiler)
}

multiplatform {
    commonMain {
        implementation(project(":core:analytics"))
        implementation(project(":core:state-holder"))
        implementation(project(":feature:spell-compendium:state-holder"))
        implementation(project(":ui:core"))
        implementation(project(":ui:compendium"))

        implementation(libs.kotlin.coroutines.core)
        implementation(libs.koin.compose)
    }
    jvmMain()
    iosMain()
}

composeCompiler {
    enableStrongSkippingMode = true
}
