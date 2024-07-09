plugins {
    id("com.android.library")
    kotlin("multiplatform")
    alias(libs.plugins.compose.compiler)
}

multiplatform {
    androidMain()
    commonMain {
        implementation(project(":feature:folder-insert:state-holder"))
        implementation(project(":ui:core"))

        implementation(libs.kotlin.coroutines.core)
        implementation(libs.koin.compose)
    }
    jvmMain()
    iosMain()
}

androidLibrary {
    namespace = "br.alexandregpereira.hunter.folder.insert"
}

composeCompiler {
    enableStrongSkippingMode = true
}
