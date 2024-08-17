plugins {
    id("com.android.library")
    kotlin("multiplatform")
    alias(libs.plugins.compose.compiler)
}

multiplatform {
    androidMain()
    commonMain {
        implementation(project(":core:dynamic-formulary"))
        implementation(project(":core:state-holder"))
        implementation(project(":domain:monster:core"))
        implementation(project(":domain:monster-lore:core"))
        implementation(project(":domain:spell:core"))
        implementation(project(":feature:monster-registration:state-holder"))
        implementation(project(":ui:core"))

        implementation(libs.kotlin.coroutines.core)
        implementation(libs.koin.compose)
    }
    jvmMain()
    iosMain()
}

androidLibrary {
    namespace = "br.alexandregpereira.hunter.monster.registration"
}

composeCompiler {
    enableStrongSkippingMode = true
}
