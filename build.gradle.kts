buildscript {
    extra.apply {
        set("compileSdk", 33)
        set("minSdk", 24)
        set("targetSdk", 33)
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
}
