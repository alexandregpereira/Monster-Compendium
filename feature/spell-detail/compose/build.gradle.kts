plugins {
    kotlin("multiplatform")
    alias(libs.plugins.compose.compiler)
}

multiplatform {
    commonMain {
        implementation(project(":core:analytics"))
        implementation(project(":core:localization"))
        implementation(project(":core:state-holder"))
        implementation(project(":domain:spell:core"))
        implementation(project(":feature:spell-detail:event"))
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
