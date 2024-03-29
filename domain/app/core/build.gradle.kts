plugins {
    kotlin("multiplatform")
}

multiplatform {
    commonMain {
        implementation(project(":domain:alternative-source:core"))
        implementation(project(":domain:monster:core"))
        implementation(project(":domain:monster-compendium:core"))
        implementation(project(":domain:monster-folder:core"))
        implementation(project(":domain:monster-lore:core"))
        implementation(project(":domain:settings:core"))
        implementation(project(":domain:spell:core"))
        implementation(project(":domain:sync:core"))
        implementation(libs.koin.core)
    }
    jvmMain()
    iosMain()
}
