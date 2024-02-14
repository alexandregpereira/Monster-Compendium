plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

multiplatform {
    commonMain {
        implementation(project(":domain:alternative-source:core"))
        implementation(project(":domain:settings:core"))
        implementation(libs.kotlin.coroutines.core)
        implementation(libs.kotlin.datetime)
        implementation(libs.kotlin.serialization)
        implementation(libs.koin.core)
        implementation(libs.ktor.core)
    }

    jvmMain {
        implementation(libs.ktor.okhttp)
    }

    iosMain {
        implementation(libs.ktor.darwin)
    }
}
