plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    namespace = "br.alexandregpereira.hunter.news"
    compileSdk = project.findProperty("compileSdk") as Int

    defaultConfig {
        minSdk = project.findProperty("minSdk") as Int
    }

    kotlinOptions {
        freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
    }

    buildFeatures.compose = true

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    implementation(project(":ui:core"))

    implementation(libs.bundles.viewmodel.bundle)
    implementation(libs.bundles.compose)

    implementation(libs.koin.compose)
}
