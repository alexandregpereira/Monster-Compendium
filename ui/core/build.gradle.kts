import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    alias(libs.plugins.compose.compiler)
}

multiplatform {
    androidMain {
        implementation(libs.core.ktx)
        implementation(libs.bundles.compose)
        implementation(libs.compose.util)
        implementation(libs.kotlin.reflect)
        implementation(libs.coil.compose)
    }
    commonMain {

    }
    jvmMain()
    iosMain()
}

android {
    namespace = "br.alexandregpereira.hunter.ui"
    compileSdk = findProperty("compileSdk")?.toString()?.toInt()

    defaultConfig {
        minSdk = findProperty("minSdk")?.toString()?.toInt()
    }

    buildFeatures {
        compose = true
    }
}

kotlin {
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    compilerOptions {
        freeCompilerArgs.add("-Xopt-in=kotlin.RequiresOptIn")
        freeCompilerArgs.add("-Xopt-in=androidx.compose.foundation.layout.ExperimentalLayoutApi")
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}
