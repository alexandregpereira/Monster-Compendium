/*
 * Copyright (C) 2024 Alexandre Gomes Pereira
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

import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.Properties

plugins {
    id("com.android.application")
    kotlin("multiplatform")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

multiplatform {
    androidMain {
        implementation(libs.core.ktx)
        implementation(libs.appcompat)
        implementation(libs.material)

        implementation(libs.koin.android)

        implementation(libs.compose.activity)

        implementation(project.dependencies.platform(libs.firebase.bom))
        implementation(libs.firebase.analytics)
        implementation(libs.firebase.crashlytics)
    }

    commonMain {
        implementation(project(":core:analytics"))
        implementation(project(":core:event"))
        implementation(project(":core:feature-flag"))
        implementation(project(":core:localization"))
        implementation(project(":core:state-holder"))
        implementation(project(":core:ui:state-recovery"))
        implementation(project(":domain:app:data"))
        implementation(project(":domain:app:core"))
        implementation(project(":domain:revenue:core"))
        implementation(project(":domain:revenue:data"))
        implementation(project(":domain:sync:core"))

        implementation(project(":feature:share-content:event"))
        implementation(project(":domain:monster:event"))

        implementation(project(":feature:folder-detail:compose"))
        implementation(project(":feature:ads:compose"))
        implementation(project(":feature:folder-insert:compose"))
        implementation(project(":feature:folder-list:compose"))
        implementation(project(":feature:folder-preview:compose"))
        implementation(project(":feature:monster-content-manager:compose"))
        implementation(project(":feature:monster-compendium:compose"))
        implementation(project(":feature:monster-detail:compose"))
        implementation(project(":feature:monster-lore-detail:compose"))
        implementation(project(":feature:monster-registration:compose"))
        implementation(project(":feature:paywall:compose"))
        implementation(project(":feature:search:compose"))
        implementation(project(":feature:settings:compose"))
        implementation(project(":feature:share-content:compose"))
        implementation(project(":feature:spell-compendium:compose"))
        implementation(project(":feature:spell-detail:compose"))
        implementation(project(":feature:sync:compose"))

        implementation(project(":ui:core"))

        implementation(libs.kotlin.coroutines.core)
        implementation(libs.koin.compose)
        implementation(libs.compose.mp.runtime)
        implementation(libs.compose.mp.foundation)
        implementation(libs.compose.mp.material)
        implementation(libs.compose.mp.ui)
        implementation(libs.compose.mp.components.resources)
        implementation(libs.compose.mp.ui.tooling.preview)
    }

    jvmMain {
        implementation(compose.desktop.currentOs)
        implementation(libs.kotlin.coroutines.desktop)
    }

    iosMain()

    jvmTest {
        implementation(libs.bundles.unittest)
        implementation(libs.koin.test)
    }
}

val (appVersionCode, appVersionName) = getVersionCodeAndVersionName()

// Load local.properties manually to ensure AMPLITUDE_API_KEY is available
val localProps = Properties()
val localPropsFile: File? = rootProject.file("local.properties")
if (localPropsFile?.exists() == true) {
    localProps.load(localPropsFile.inputStream())
}

tasks.register<GenerateAppConfigTask>("generateAppConfig") {
    val isDebug = project.gradle.startParameter.taskNames.any { it.contains("Release") }.not()
    val versionNameSuffix = when {
        project.hasProperty("dev") -> {
            "-dev"
        }
        isDebug -> {
            "-debug"
        }
        else -> ""
    }

    val isSandboxBuild = project.hasProperty("dev") || isDebug
    // Read AMPLITUDE_API_KEY from environment, local.properties, or use empty string
    val amplitudeApiKeyEnvVarName = if (isSandboxBuild) {
        "AMPLITUDE_SANDBOX_API_KEY"
    } else {
        "AMPLITUDE_API_KEY"
    }
    val envVar = System.getenv(amplitudeApiKeyEnvVarName)?.takeIf { it.isNotEmpty() }
    val propVar = localProps.getProperty(amplitudeApiKeyEnvVarName)?.takeIf { it.isNotEmpty() }
    val amplitudeApiKey = envVar ?: propVar ?: ""

    // Read REVENUE_CAT_API_KEY from environment or use empty string
    val revenueCatApiKeyEnvVarName = "REVENUE_CAT_API_KEY"
    val revenueCatEnvVarApiKey = System.getenv(revenueCatApiKeyEnvVarName)?.takeIf { it.isNotEmpty() }
    val revenueCatPropVarApiKey = localProps.getProperty(revenueCatApiKeyEnvVarName)?.takeIf { it.isNotEmpty() }
    val revenueCatApiKeyFinal = revenueCatEnvVarApiKey ?: revenueCatPropVarApiKey ?: ""

    val amplitudeKeyType = if (isSandboxBuild) "sandbox" else "production"
    logger.quiet("Using Amplitude API Key ($amplitudeKeyType): ${if (amplitudeApiKey.isNotEmpty()) "****" else "(none)"}")

    taskVersionName.set(appVersionName + versionNameSuffix)
    taskVersionCode.set(appVersionCode)
    taskVersionNameSuffix.set(versionNameSuffix)
    taskAmplitudeApiKey.set(amplitudeApiKey)
    taskRevenueCatApiKey.set(revenueCatApiKeyFinal)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            val srcPath = tasks.named<GenerateAppConfigTask>(name = "generateAppConfig").flatMap {
                it.outputDir
            }
            kotlin.srcDir(srcPath)
        }
    }
}

android {
    namespace = "br.alexandregpereira.hunter.app"
    compileSdk = findProperty("compileSdk")?.toString()?.toInt()

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
        minSdk = findProperty("minSdk")?.toString()?.toInt()
        targetSdk = findProperty("targetSdk")?.toString()?.toInt()
        applicationIdSuffix = when {
            hasProperty("dev") -> {
                ".dev"
            }
            else -> ""
        }

        versionCode = appVersionCode
        versionName = appVersionName

        val admobAppIdEnvVarName = "ADMOB_APP_ID"
        val admobAppIdEnvVar = System.getenv(admobAppIdEnvVarName)?.takeIf { it.isNotEmpty() }
        val admobAppIdPropVar = localProps.getProperty(admobAppIdEnvVarName)?.takeIf { it.isNotEmpty() }
        val admobAppId = admobAppIdEnvVar ?: admobAppIdPropVar ?: "ca-app-pub-3940256099942544~3347511713"
        manifestPlaceholders["ADMOB_APP_ID"] = admobAppId

        if (hasProperty("dev")) {
            setProperty("archivesBaseName", "app-dev")
        }

        testInstrumentationRunner = "br.alexandregpereira.hunter.app.KoinTestRunner"
    }

    signingConfigs {
        create("release") {
            storeFile = file("monster-keystore.jks")
            storePassword = System.getenv("MONSTER_COMPENDIUM_KEYSTORE_PASSWORD")
            keyAlias = "monster"
            keyPassword = System.getenv("MONSTER_COMPENDIUM_KEYSTORE_PASSWORD")
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
    dependencies {
        debugImplementation(libs.compose.mp.ui.tooling)
        androidTestImplementation(libs.bundles.instrumentedtest)
        androidTestImplementation(libs.compose.ui.test)
        androidTestImplementation(libs.sqldelight.android)
        androidTestImplementation(libs.multiplatform.settings)
        androidTestImplementation(libs.multiplatform.settings.test)
        debugImplementation(libs.compose.ui.test.manifest)
    }
}

compose {
    desktop {
        application {
            mainClass = "MainKt"

            nativeDistributions {
                modules("java.sql", "java.net.http")
                targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Rpm)
                packageName = "D&D Monster Compendium"
                packageVersion = appVersionName
                description = "A Dungeons & Dragons 5th edition monster compendium"
                copyright = "Copyright (C) 2025 Alexandre Gomes Pereira"
                vendor = "Alexandre Gomes Pereira"
                licenseFile.set(project.file("../LICENSE"))

                macOS {
                    iconFile.set(project.file("icon-mac.icns"))
                }
                windows {
                    iconFile.set(project.file("icon-windows.ico"))
                }
                linux {
                    iconFile.set(project.file("icon-linux.png"))
                    packageName = "dnd-monster-compendium"
                }
            }

            buildTypes.release.proguard {
                configurationFiles.from(project.file("compose-desktop.pro"))
            }
        }
    }
    resources {
        publicResClass = false
        packageOfResClass = "br.alexandregpereira.hunter.app.ui.resources"
        generateResClass = always
    }
}

// Ensure the task runs before compilation
tasks.withType<KotlinCompile>().configureEach {
    dependsOn("generateAppConfig")
}