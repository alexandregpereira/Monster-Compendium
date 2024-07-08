plugins {
    kotlin("multiplatform")
}

multiplatform {
    commonMain {
        api(project(":core:flow:core"))
    }
    jvmMain()
    iosMain()
}
