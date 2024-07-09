plugins {
    id("com.android.library")
    kotlin("multiplatform")
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

multiplatform {
    androidMain {
        implementation(libs.core.ktx)
        implementation(libs.compose.activity)
        implementation(libs.compose.tooling.preview)
        implementation(libs.kotlin.reflect)
    }
    commonMain {
        api(compose.ui)
        api(compose.material)
        api(compose.uiUtil)
        api(compose.components.resources)
        api(compose.components.uiToolingPreview)
        implementation(libs.ktor.core)
        implementation(libs.coil.compose.core)
        implementation(libs.coil.compose)
        implementation(libs.coil.mp)
        implementation(libs.coil.network.ktor)
    }
    jvmMain()
    iosMain()
}

androidLibrary {
    namespace = "br.alexandregpereira.hunter.ui"
}

composeCompiler {
    enableStrongSkippingMode = true
}

compose.resources {
    publicResClass = true
    packageOfResClass = "br.alexandregpereira.hunter.ui.resources"
    generateResClass = always
}
