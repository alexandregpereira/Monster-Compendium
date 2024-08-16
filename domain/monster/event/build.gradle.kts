plugins {
    kotlin("multiplatform")
}

multiplatform {
    commonMain {
        implementation(project(":feature:sync:event"))
        implementation(project(":feature:monster-registration:event"))
        implementation(libs.koin.core)
        implementation(libs.kotlin.coroutines.core)
    }
    jvmMain()
    iosMain()
}
