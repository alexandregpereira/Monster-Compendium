plugins {
    kotlin("multiplatform")
}

multiplatform {
    commonMain {
        implementation(project(":feature:sync:event"))
        implementation(libs.koin.core)
        implementation(libs.kotlin.coroutines.core)
    }
    jvmMain()
    iosMain()
}
