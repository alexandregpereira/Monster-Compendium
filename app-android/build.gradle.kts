/*
 * Copyright (C) 2026 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

import com.android.build.api.artifact.SingleArtifact
import java.util.Properties

plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    alias(libs.plugins.compose.compiler)
}

val (appVersionCode, appVersionName) = getVersionCodeAndVersionName()

val localProps = Properties()
val localPropsFile: File = rootProject.file("local.properties")
if (localPropsFile.exists()) {
    localProps.load(localPropsFile.inputStream())
}

val isRelease = project.gradle.startParameter.taskNames.any { it.contains("Release") }
    || System.getenv("CONFIGURATION") == "Release"
val isDebug = !isRelease

fun getEnvVar(prodKey: String, sandboxKey: String = prodKey, homologKey: String = sandboxKey, fallback: String = ""): String {
    val isDevBuild = project.hasProperty("dev")
    val key = when {
        isDevBuild -> homologKey
        isRelease -> prodKey
        else -> sandboxKey
    }
    val envVar = System.getenv(key)?.takeIf { it.isNotEmpty() }
    val propVar = localProps.getProperty(key)?.takeIf { it.isNotEmpty() }
    val keyValue = envVar ?: propVar ?: fallback
    if (keyValue.isBlank()) {
        logger.warn("Warning: Missing environment variable $key")
    }
    return keyValue
}

android {
    namespace = "br.alexandregpereira.hunter.app"
    compileSdk = findProperty("android.compileSdk")?.toString()?.toInt()

    packaging {
        jniLibs {
            excludes += "META-INF/licenses/**"
        }
        resources {
            excludes += "**/attach_hotspot_windows.dll"
            excludes += "META-INF/licenses/**"
            excludes += "META-INF/AL2.0"
            excludes += "META-INF/LGPL2.1"
            excludes += "META-INF/LICENSE**"
        }
    }

    defaultConfig {
        applicationId = "br.alexandregpereira.hunter.app"
        minSdk = findProperty("android.minSdk")?.toString()?.toInt()
        applicationIdSuffix = when {
            hasProperty("dev") -> ".dev"
            else -> ""
        }

        versionCode = appVersionCode
        versionName = appVersionName

        val admobAppId = getEnvVar(
            prodKey = "ADMOB_APP_ID",
            sandboxKey = "ADMOB_SANDBOX_APP_ID",
            fallback = "ca-app-pub-3940256099942544~3347511713",
        )
        manifestPlaceholders["ADMOB_APP_ID"] = admobAppId

        testInstrumentationRunner = "br.alexandregpereira.hunter.app.KoinTestRunner"
    }

    signingConfigs {
        val appKeyPassword = getEnvVar(prodKey = "MONSTER_COMPENDIUM_KEYSTORE_PASSWORD")
        create("release") {
            storeFile = file("monster-keystore.jks")
            storePassword = appKeyPassword
            keyAlias = "monster"
            keyPassword = appKeyPassword
        }
    }

    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(project(":app"))
    implementation(project(":core:ads-consent"))
    implementation(project(":core:feature-flag"))
    implementation(project(":core:state-holder"))
    implementation(project(":ui:core"))
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.koin.android)
    implementation(libs.compose.activity)
    implementation(project.dependencies.platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    debugImplementation(libs.compose.mp.ui.tooling)
    androidTestImplementation(libs.bundles.instrumentedtest)
    androidTestImplementation(libs.compose.ui.test)
    androidTestImplementation(libs.sqldelight.android)
    androidTestImplementation(libs.multiplatform.settings)
    androidTestImplementation(libs.multiplatform.settings.test)
    debugImplementation(libs.compose.ui.test.manifest)
}

androidComponents {
    onVariants { variant ->
        val buildTypeName = variant.buildType ?: ""
        val prefix = if (project.hasProperty("dev")) "app-dev" else "app"
        val variantNameCapitalized = variant.name.replaceFirstChar { it.uppercase() }

        val renameTask = tasks.register<RenameApkTask>("rename${variantNameCapitalized}Apk") {
            apkPrefix.set(prefix)
            apkBuildType.set(buildTypeName)
            apkVersionName.set(appVersionName)
        }

        val transformationRequest = variant.artifacts.use(renameTask)
            .wiredWithDirectories(RenameApkTask::input, RenameApkTask::output)
            .toTransformMany(SingleArtifact.APK)

        renameTask.configure {
            this.transformationRequest = transformationRequest
        }
    }
}
