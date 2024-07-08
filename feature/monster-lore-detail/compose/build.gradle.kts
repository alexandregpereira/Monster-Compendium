plugins {
    id("com.android.library")
    kotlin("multiplatform")
    alias(libs.plugins.compose.compiler)
}

multiplatform {
    androidMain()
    commonMain {
        implementation(project(":core:ui:state-recovery"))
        implementation(project(":feature:monster-lore-detail:state-holder"))
        implementation(project(":ui:core"))

        implementation(libs.kotlin.coroutines.core)
        implementation(libs.koin.compose)
    }
    jvmMain()
    iosMain()
}

androidLibrary {
    namespace = "br.alexandregpereira.hunter.monster.lore.detail"
}

composeCompiler {
    enableStrongSkippingMode = true
}
