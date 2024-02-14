plugins {
    kotlin("multiplatform")
}

multiplatform {
    commonMain {
        implementation(project(":domain:monster:core"))
        implementation(project(":domain:monster-lore:core"))
        implementation(project(":domain:alternative-source:core"))
        implementation(project(":domain:spell:core"))
        implementation(project(":domain:settings:core"))
        implementation(libs.koin.core)
        implementation(libs.kotlin.coroutines.core)
    }
    jvmMain()
    iosMain()
}
