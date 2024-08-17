plugins {
    id("com.android.library")
    kotlin("multiplatform")
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

multiplatform {
    androidMain()
    commonMain {
        implementation(project(":core:state-holder"))
        implementation(project(":core:ui:state-recovery"))
        implementation(project(":domain:monster:core"))
        implementation(project(":domain:monster-lore:core"))
        implementation(project(":domain:spell:core"))
        implementation(project(":feature:folder-insert:event"))
        implementation(project(":domain:monster:event"))
        implementation(project(":feature:monster-lore:event"))
        implementation(project(":feature:monster-registration:event"))
        implementation(project(":feature:spell-detail:event"))
        implementation(project(":feature:sync:event"))
        implementation(project(":feature:monster-detail:state-holder"))
        implementation(project(":ui:core"))

        implementation(libs.coil.compose)
        implementation(libs.koin.compose)
        implementation(libs.kotlin.reflect)
        implementation(libs.kotlin.coroutines.core)
    }
    jvmMain()
    iosMain()
}

androidLibrary {
    namespace = "br.alexandregpereira.hunter.detail"
}

composeCompiler {
    enableStrongSkippingMode = true
}

compose.resources {
    publicResClass = false
    packageOfResClass = "br.alexandregpereira.hunter.detail.ui.resources"
    generateResClass = always
}
