plugins {
    kotlin("multiplatform")
}

multiplatform {
    commonMain {
        api(project(":core:flow"))
    }
    jvmMain()
    iosMain()
}
