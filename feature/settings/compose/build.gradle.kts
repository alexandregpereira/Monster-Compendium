plugins {
    id("com.android.library")
    kotlin("multiplatform")
    alias(libs.plugins.compose.compiler)
}

multiplatform {
    androidMain()
    commonMain {
        implementation(project(":core:analytics"))
        implementation(project(":core:event"))
        implementation(project(":core:localization"))
        implementation(project(":core:state-holder"))
        implementation(project(":domain:monster:core"))
        implementation(project(":domain:monster:event"))
        implementation(project(":domain:settings:core"))
        implementation(project(":feature:share-content:event"))
        implementation(project(":feature:monster-content-manager:event"))
        implementation(project(":feature:sync:event"))
        implementation(project(":ui:core"))

        implementation(libs.kotlin.coroutines.core)
        implementation(libs.koin.compose)
    }
    jvmMain()
    iosMain()
}

androidLibrary {
    namespace = "br.alexandregpereira.hunter.settings"
}

composeCompiler {
    enableStrongSkippingMode = true
}
