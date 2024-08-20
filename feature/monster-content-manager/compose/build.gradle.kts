plugins {
    id("com.android.library")
    kotlin("multiplatform")
    alias(libs.plugins.compose.compiler)
}

multiplatform {
    androidMain()
    commonMain {
        implementation(project(":core:ui:state-recovery"))
        implementation(project(":core:uuid"))
        implementation(project(":feature:monster-content-manager:state-holder"))
        implementation(project(":ui:core"))
        implementation(project(":ui:monster-compendium"))

        implementation(libs.kotlin.coroutines.core)
        implementation(libs.koin.compose)
    }
    jvmMain()
    iosMain()
}

androidLibrary {
    namespace = "br.alexandregpereira.hunter.monster.content"
}

composeCompiler {
    enableStrongSkippingMode = true
}
