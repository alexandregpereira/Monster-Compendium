plugins {
    id("com.android.library")
    kotlin("multiplatform")
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

multiplatform {
    androidMain {
        implementation(libs.bundles.viewmodel.bundle)
        implementation(libs.core.ktx)
        implementation(libs.compose.activity)
        implementation(libs.compose.tooling.preview)
        implementation(libs.koin.android.compose)
    }
    commonMain {
        implementation(project(":core:localization"))
        implementation(project(":feature:folder-preview:event"))
        implementation(project(":feature:monster-detail:event"))
        implementation(project(":feature:monster-registration:event"))
        implementation(project(":feature:sync:event"))
        implementation(project(":feature:monster-compendium:state-holder"))
        implementation(project(":ui:core"))
        implementation(project(":ui:monster-compendium"))

        implementation(libs.koin.compose)
        implementation(libs.kotlin.reflect)
        implementation(libs.kotlin.coroutines.core)
    }
    jvmMain()
    iosMain()
}

androidLibrary {
    namespace = "br.alexandregpereira.hunter.monster.compendium"
}
