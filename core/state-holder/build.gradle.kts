plugins {
    kotlin("multiplatform")
}

multiplatform {
    commonMain {
        api(project(":core:flow:core"))
        implementation(project(":core:ui:state-recovery"))
    }
    jvmMain()
    iosMain()
}
