plugins {
    kotlin("multiplatform")
}

multiplatform {
    commonMain {
        implementation(project(":core:analytics"))
        implementation(project(":core:event"))
        implementation(project(":core:localization"))
        api(project(":core:state-holder"))
        api(project(":core:ui:state-recovery"))
        api(project(":domain:monster-compendium:core"))
        implementation(project(":feature:folder-preview:event"))
        implementation(project(":feature:monster-detail:event"))
        implementation(project(":feature:sync:event"))
        implementation(project(":feature:monster-registration:event"))
        implementation(libs.kotlin.coroutines.core)
        implementation(libs.koin.core)
    }
    commonTest {
        implementation(kotlin("test"))
        implementation(project(":core:flow:test"))
        implementation(libs.kotlin.coroutines.test)
    }
    jvmMain()
    iosMain()
}
