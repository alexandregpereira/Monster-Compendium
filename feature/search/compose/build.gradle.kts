plugins {
    id("com.android.library")
    kotlin("multiplatform")
    alias(libs.plugins.compose.compiler)
}

multiplatform {
    androidMain()
    commonMain {
        implementation(project(":core:analytics"))
        implementation(project(":core:localization"))
        implementation(project(":core:state-holder"))
        implementation(project(":domain:monster:core"))
        implementation(project(":domain:spell:core"))
        implementation(project(":feature:folder-preview:event"))
        implementation(project(":feature:monster-detail:event"))
        implementation(project(":ui:core"))
        implementation(project(":ui:monster-compendium"))

        implementation(libs.kotlin.coroutines.core)
        implementation(libs.koin.compose)
    }
    jvmMain()
    iosMain()
}

androidLibrary {
    namespace = "br.alexandregpereira.hunter.search"
}

composeCompiler {
    enableStrongSkippingMode = true
}