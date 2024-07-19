plugins {
    id("com.android.library")
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    alias(libs.plugins.compose.compiler)
}

multiplatform {
    androidMain()
    commonMain {
        implementation(project(":domain:monster:core"))
        implementation(project(":domain:monster-lore:core"))
        implementation(project(":domain:spell:core"))
        implementation(project(":core:localization"))
        implementation(project(":core:analytics"))
        implementation(project(":core:event"))
        implementation(project(":core:state-holder"))
        implementation(project(":feature:share-content:event"))
        implementation(project(":ui:core"))

        implementation(libs.kotlin.coroutines.core)
        implementation(libs.koin.compose)
        implementation(libs.kotlin.serialization)
    }
    jvmMain()
    jvmTest {
        implementation(kotlin("test"))
        implementation(libs.kotlin.coroutines.test)
    }
    iosMain()
}

androidLibrary {
    namespace = "br.alexandregpereira.hunter.shareContent"
}

composeCompiler {
    enableStrongSkippingMode = true
}
