buildscript {
    extra.apply {
        set("compileSdk", 34)
        set("minSdk", 24)
        set("targetSdk", 34)
    }
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.gradle.android)
        classpath(libs.gradle.kotlin)
        classpath(libs.gradle.googleplay.services)
        classpath(libs.gradle.firebase.crashlytics)
    }
}

plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.compose.compiler) apply false
}
