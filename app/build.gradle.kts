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
import java.util.Properties

plugins {
    id("com.android.kotlin.multiplatform.library")
    kotlin("multiplatform")
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

multiplatform {
    androidMain()

    commonMain {
        implementation(project(":core:ads-consent"))
        implementation(project(":core:analytics"))
        implementation(project(":core:app-config"))
        implementation(project(":core:event"))
        implementation(project(":core:feature-flag"))
        implementation(project(":core:file:core"))
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
        implementation(project(":domain:spell:event"))

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
        implementation(project(":feature:spell-registration:compose"))
        implementation(project(":feature:sync:compose"))

        implementation(project(":ui:color-picker"))
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

// Load local.properties manually to ensure AMPLITUDE_API_KEY is available
val localProps = Properties()
val localPropsFile: File = rootProject.file("local.properties")
if (localPropsFile.exists()) {
    localProps.load(localPropsFile.inputStream())
}

val isIos = System.getenv("PLATFORM_NAME") != null
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

val (appVersionCode, appVersionName) = getVersionCodeAndVersionName()

tasks.register<UpdateIosVersionTask>("updateIosVersion") {
    versionName.set(appVersionName)
    versionCode.set(appVersionCode)
    iosAppDir.set(rootProject.layout.projectDirectory.dir("iosApp"))
    admobAppId.set(
        getEnvVar(
            prodKey = "ADMOB_IOS_APP_ID",
            sandboxKey = "ADMOB_SANDBOX_IOS_APP_ID",
            fallback = "ca-app-pub-3940256099942544~3347511713",
        )
    )
}

tasks.matching { it.name.contains("embedAndSignAppleFrameworkForXcode") }
    .configureEach { dependsOn("updateIosVersion") }

tasks.register<GenerateAppConfigTask>("generateAppConfig") {
    val versionNameSuffix = when {
        project.hasProperty("dev") -> {
            "-dev"
        }
        isDebug -> {
            "-debug"
        }
        else -> ""
    }

    val amplitudeApiKey = getEnvVar(
        prodKey = "AMPLITUDE_API_KEY",
        sandboxKey = "AMPLITUDE_SANDBOX_API_KEY",
    )
    val revenueCatApiKey = when {
        isIos -> getEnvVar(
            prodKey = "REVENUE_CAT_IOS_API_KEY",
            homologKey = "REVENUE_CAT_IOS_API_KEY",
            sandboxKey = "REVENUE_CAT_SANDBOX_API_KEY",
        )
        else -> getEnvVar(
            prodKey = "REVENUE_CAT_ANDROID_API_KEY",
            homologKey = "REVENUE_CAT_ANDROID_API_KEY",
            sandboxKey = "REVENUE_CAT_SANDBOX_API_KEY",
        )
    }

    taskVersionName.set(appVersionName + versionNameSuffix)
    taskVersionCode.set(appVersionCode)
    taskVersionNameSuffix.set(versionNameSuffix)
    taskAmplitudeApiKey.set(amplitudeApiKey)
    taskRevenueCatApiKey.set(revenueCatApiKey)
    taskAdsConsentDeviceHashTestId.set(findProperty("eeaDeviceId")?.toString() ?: "")
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

androidLibrary {
    namespace = "br.alexandregpereira.hunter.app.lib"
}

configureComposeAssetsForAndroidMain("br.alexandregpereira.hunter.app.ui.resources")

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
