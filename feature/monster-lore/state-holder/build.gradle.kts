plugins {
    kotlin("multiplatform")
}

multiplatform {
    commonMain {
        implementation(project(":core:analytics"))
        implementation(project(":core:dynamic-formulary"))
        implementation(project(":core:ui:state-recovery"))
        implementation(project(":core:localization"))
        implementation(project(":core:uuid"))
        api(project(":core:state-holder"))
        api(project(":domain:monster-lore:core"))
        implementation(project(":domain:monster:core"))
        implementation(project(":feature:monster-lore:event"))
        implementation(libs.kotlin.coroutines.core)
        implementation(libs.koin.core)
    }
    jvmMain()
    iosMain()
}
