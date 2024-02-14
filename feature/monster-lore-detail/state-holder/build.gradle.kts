plugins {
    kotlin("multiplatform")
}

multiplatform {
    commonMain {
        implementation(project(":core:analytics"))
        api(project(":core:state-holder"))
        api(project(":domain:monster-lore:core"))
        implementation(project(":domain:monster:core"))
        implementation(project(":feature:monster-lore-detail:event"))
        implementation(libs.kotlin.coroutines.core)
        implementation(libs.koin.core)
    }
    jvmMain()
    iosMain()
}
