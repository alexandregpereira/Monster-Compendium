plugins {
    kotlin("multiplatform")
}

multiplatform {
    commonMain {
        implementation(project(":core:analytics"))
        implementation(project(":core:localization"))
        implementation(project(":core:ui:state-recovery"))
        api(project(":core:state-holder"))
        api(project(":domain:monster-folder:core"))
        implementation(project(":feature:folder-preview:event"))
        implementation(project(":feature:folder-insert:event"))
        implementation(project(":feature:monster-detail:event"))
        implementation(libs.kotlin.coroutines.core)
        implementation(libs.koin.core)
    }
    jvmMain()
    iosMain()
}
