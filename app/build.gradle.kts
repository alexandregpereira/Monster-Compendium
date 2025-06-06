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
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

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
        implementation(project(":core:localization"))
        implementation(project(":core:state-holder"))
        implementation(project(":core:ui:state-recovery"))
        implementation(project(":domain:app:data"))
        implementation(project(":domain:app:core"))
        implementation(project(":domain:sync:core"))

        implementation(project(":feature:share-content:event"))
        implementation(project(":domain:monster:event"))

        implementation(project(":feature:folder-detail:compose"))
        implementation(project(":feature:folder-insert:compose"))
        implementation(project(":feature:folder-list:compose"))
        implementation(project(":feature:folder-preview:compose"))
        implementation(project(":feature:monster-content-manager:compose"))
        implementation(project(":feature:monster-compendium:compose"))
        implementation(project(":feature:monster-detail:compose"))
        implementation(project(":feature:monster-lore-detail:compose"))
        implementation(project(":feature:monster-registration:compose"))
        implementation(project(":feature:search:compose"))
        implementation(project(":feature:settings:compose"))
        implementation(project(":feature:share-content:compose"))
        implementation(project(":feature:spell-compendium:compose"))
        implementation(project(":feature:spell-detail:compose"))
        implementation(project(":feature:sync:compose"))

        implementation(project(":ui:core"))

        implementation(libs.kotlin.coroutines.core)
        implementation(libs.koin.compose)
        implementation(compose.runtime)
        implementation(compose.foundation)
        implementation(compose.material)
        implementation(compose.ui)
        implementation(compose.components.resources)
        implementation(compose.components.uiToolingPreview)
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

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        instrumentedTestVariant {
            sourceSetTree.set(KotlinSourceSetTree.test)

            dependencies {
                implementation(libs.bundles.instrumentedtest)
                implementation(libs.compose.ui.test)
                implementation(libs.sqldelight.android)
                implementation(libs.multiplatform.settings)
                implementation(libs.multiplatform.settings.test)
                debugImplementation(libs.compose.ui.test.manifest)
            }
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

    val isDebug = project.gradle.startParameter.taskNames.any { it.contains("Debug") }
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

        val localDateTime = Instant.now().atOffset(ZoneOffset.UTC).toLocalDateTime()
        val baseTimestampInSeconds = 1467504000L // Base value for the timestamp of 2013-07-03 00:00:00
        val currentTimestampInSeconds = localDateTime.toEpochSecond(ZoneOffset.UTC)
        val timestampDiff = currentTimestampInSeconds - baseTimestampInSeconds
        if (timestampDiff <= 0) {
            throw RuntimeException("Adjust your machine datetime! timestampDiff")
        }
        val versionCodePerSeconds = 60 * 20 // Seconds to increment the version code
        versionCode = (timestampDiff / versionCodePerSeconds).toInt().also { versionCode ->
            val dateFormat = localDateTime.format(DateTimeFormatter.ofPattern("yy.MM.dd."))
            versionName = dateFormat + (versionCode - 186000)
        }

        versionNameSuffix = when {
            hasProperty("dev") -> {
                "-dev"
            }
            isDebug -> {
                "-debug"
            }
            else -> ""
        }
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
        debugImplementation(compose.uiTooling)
    }
}

compose {
    desktop {
        application {
            mainClass = "MainKt"

            nativeDistributions {
                modules("java.sql", "java.net.http")
                targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
                packageName = "DnD Monster Compendium"
                packageVersion = "1.0.0"

                macOS {
                    iconFile.set(project.file("icon-mac.icns"))
                }
                windows {
                    iconFile.set(project.file("icon-windows.ico"))
                }
                linux {
                    iconFile.set(project.file("icon-linux.png"))
                }
            }
        }
    }
    resources {
        publicResClass = false
        packageOfResClass = "br.alexandregpereira.hunter.app.ui.resources"
        generateResClass = always
    }
}
