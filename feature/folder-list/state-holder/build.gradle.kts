plugins {
    kotlin("multiplatform")
}

multiplatform {
    commonMain {
        implementation(project(":core:analytics"))
        implementation(project(":core:localization"))
        api(project(":core:state-holder"))
        api(project(":domain:monster-folder:core"))
        implementation(project(":feature:folder-insert:event"))
        implementation(project(":feature:folder-detail:event"))
        implementation(project(":feature:folder-list:event"))
        implementation(project(":feature:sync:event"))
        implementation(libs.kotlin.coroutines.core)
        implementation(libs.koin.core)
    }
    jvmMain()
    iosMain()
}
