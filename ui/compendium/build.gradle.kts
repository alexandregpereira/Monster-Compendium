plugins {
    id("com.android.library")
    kotlin("multiplatform")
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

multiplatform {
    androidMain()
    commonMain {
        implementation(project(":ui:core"))
    }
    jvmMain()
    iosMain()
}

androidLibrary {
    namespace = "br.alexandregpereira.hunter.ui.compendium"
}
