plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

multiplatform {
    androidMain {
        implementation(libs.viewmodel.savedstate)
    }

    commonMain {
        implementation(project(":core:analytics"))
        implementation(project(":core:localization"))
        api(project(":core:state-holder"))
        api(project(":domain:alternative-source:core"))
        api(project(":domain:monster:core"))
        api(project(":domain:monster-compendium:core"))
        implementation(project(":feature:monster-content-manager:event"))
        implementation(project(":feature:sync:event"))
        implementation(libs.kotlin.coroutines.core)
        implementation(libs.koin.core)
    }
    jvmMain()
    iosMain()
}

android {
    namespace = "br.alexandregpereira.hunter.monster.content.state"
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()
    }
}
